package com.app.crawler.service;

import com.app.crawler.dto.ProductDto;
import com.app.crawler.dto.ProductViewDto;
import com.app.crawler.entity.*;
import com.app.crawler.repository.*;
import com.app.crawler.request.ProductSearchRequest;
import com.app.crawler.response.ProductList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    private static final String LAPTOP_PAGE_URL = "https://tiki.vn/api/v2/products?limit=40&include=advertisement&aggregations=2&trackity_id=55fbf0bf-2b8e-1244-ee85-1a1a8316ee5b&page=%d&q=laptop";
    private static final String PRODUCT_URL = "https://tiki.vn/api/v2/products/%s";
    private static final String USER_AGENT = "https://tiki.vn/api/v2/products/%sMozilla/5.0 (Macintosh; Intel Mac OS X 11_1_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.96 Safari/537.36";

    private boolean dataCrawled = false;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SpecificationsRepository specificationsRepository;

    @Autowired
    private CategoriesRepository categoriesRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private SellerRepository sellerRepository;

    public void startCrawling() {
        dataCrawled = true; //set the flag variable to true to prevent re-execution
        List<String> productIds = crawlProductIds();
        for (String productId : productIds) {
            Product product = fetchProduct(productId);
            if (product != null && !productRepository.existsById(product.getId())) {
                productRepository.save(product);
            }
        }
    }

    private List<String> crawlProductIds() {
        List<String> productIds = new ArrayList<>();
        int page = 1;
        while (true) {
            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", USER_AGENT);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            String url = String.format(LAPTOP_PAGE_URL, page);

            ResponseEntity<ProductList> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, ProductList.class);

            ProductList productList = responseEntity.getBody();
            if (productList == null || productList.getData() == null || productList.getData().isEmpty()) {
                break;
            }

            for (ProductDto productDto : productList.getData()) {
                productIds.add(productDto.getId());
            }

            page++;
        }
        return productIds;
    }

    private Product fetchProduct(String productId) {
        String url = String.format(PRODUCT_URL, productId);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            String jsonResult = responseEntity.getBody();
            if (jsonResult != null && jsonResult.trim().startsWith("{") && jsonResult.trim().endsWith("}")) {
                try {
                    JSONObject jsonObject= new JSONObject(jsonResult);
                    Product productToSave = new Product(jsonObject);

                    JSONArray categoryJArray= jsonObject.getJSONArray("breadcrumbs");
                    List<Categories> categories = new ArrayList<>();
                    List<Categories> categoriesListToSave = new ArrayList<>();
                    for (int i = 0; i < categoryJArray.length(); i++) {
                        JSONObject categoryJObject = categoryJArray.getJSONObject(i);
                        Categories category = new Categories(categoryJObject);
                        categories.add(category);
                        if (!categoriesRepository.existsById(category.getId())) {
                            categoriesListToSave.add(category);
                        }
                    }
                    categoriesRepository.saveAll(categoriesListToSave);

                    JSONObject brandJObject = jsonObject.getJSONObject("brand");
                    Brand brandToSave = new Brand(brandJObject);
                    if  (!brandRepository.existsById(brandToSave.getId())) {
                        brandRepository.save(brandToSave);
                    }

                    JSONObject sellerJObject = jsonObject.getJSONObject("current_seller");
                    Seller sellerToSave = new Seller(sellerJObject);
                    if  (!sellerRepository.existsById(sellerToSave.getId())) {
                        sellerRepository.save(sellerToSave);
                    }

                    JSONArray specificationsJArray= jsonObject.getJSONArray("specifications");
                    List<Specifications> specifications = new ArrayList<>();
                    List<Specifications> specificationsListToSave = new ArrayList<>();
                    for (int i = 0; i < specificationsJArray.length(); i++) {
                        JSONObject specificationsJObject = specificationsJArray.getJSONObject(i);
                        JSONArray attributesJArray = specificationsJObject.getJSONArray("attributes");
                        for (int j = 0; j < attributesJArray.length(); j++) {
                            JSONObject attributesJObject= attributesJArray.getJSONObject(j);
                            Specifications specification = new Specifications(attributesJObject);
                            specifications.add(specification);
                            if (!specificationsRepository.existsByCodeAndValue(specification.getCode(), specification.getValue())) {
                                specificationsListToSave.add(specification);
                            }
                        }
                        specificationsRepository.saveAll(specificationsListToSave);
                    }

                    productToSave.setCategories(categories);
                    productToSave.setBrand(brandToSave);
                    productToSave.setSeller(sellerToSave);
                    productToSave.setSpecifications(specifications);
                    return productToSave;
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            } else {
                System.out.println("Data not valid JSONObject, skipping processing...");
            }
        } else {
            System.out.println("Error occurred: " + responseEntity.getStatusCode());
        }
        return null;
    }

    public boolean checkSaveData() {
        return dataCrawled || productRepository.count() >= 966;
    }

    public Page<ProductViewDto> findAllByOrderByIdAsc(Pageable pageable) {
        List<Product> products = productRepository.findAllByOrderByIdAsc(pageable).getContent();
        return new PageImpl<>(products.stream()
                .map(ProductViewDto::new)
                .toList());
    }

    public List<ProductViewDto> findPageProductsBySearchForm(ProductSearchRequest product) {
        List<String> brandIds = product.getBrands().stream().map(Brand::getId).toList();
        List<Product> products = productRepository.findProductsByNameAndPriceAndBrandIds(product.getName(), product.getMinBudget(), product.getMaxBudget(), brandIds);
        return products.stream()
                .map(ProductViewDto::new)
                .toList();
    }
}
