package org.lordrose.vrms.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import lombok.RequiredArgsConstructor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.lordrose.vrms.constants.DetectionValues;
import org.lordrose.vrms.models.CategoryObject;
import org.lordrose.vrms.models.requests.FindProviderWithCategoryRequest;
import org.lordrose.vrms.models.responses.DetectedCategoryResponse;
import org.lordrose.vrms.repositories.PartCategoryRepository;
import org.lordrose.vrms.services.ProviderSuggestingService;
import org.lordrose.vrms.utils.distances.GeoPoint;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ImageDetectionServiceImpl {

    private static final String CONTENT_TYPE = "application/octet-stream";
    private static final String FORM_KEY = "file";
    private static final MediaType TYPE = MediaType.parse(CONTENT_TYPE);
    private static final String URL = "http://127.0.0.1:5000/detect_image";

    private final DetectionValues.ValueMapping mapper;
    private final PartCategoryRepository categoryRepository;

    private final ProviderSuggestingService suggestingService;

    public List<DetectedCategoryResponse> getCategoryId(MultipartFile image) {
        try {
            return processImage(image);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Object findProvidersWithParts(Long modelId, GeoPoint currentPos, MultipartFile image) {
        try {
            FindProviderWithCategoryRequest request = new FindProviderWithCategoryRequest();
            request.setCategoryIds(processImage(image).stream()
                    .map(DetectedCategoryResponse::getCategoryId)
                    .collect(Collectors.toSet()));
            request.setCurrentPos(currentPos);
            request.setModelId(modelId);
            return suggestingService.findProviders(request);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private List<DetectedCategoryResponse> processImage(MultipartFile image) throws Exception {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MultipartBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                        FORM_KEY,
                        image.getOriginalFilename(),
                        RequestBody.create(image.getBytes(), TYPE))
                .build();
        Request request = new Request.Builder()
                .url(URL)
                .method("POST", body)
                .build();
        ResponseBody response = client.newCall(request).execute().body();
        ObjectMapper mapper = new ObjectMapper();
        CollectionType type = mapper.getTypeFactory()
                .constructCollectionType(List.class, CategoryObject.class);

        assert response != null;

        List<CategoryObject> detectedCategories = mapper.readValue(response.string(), type);

        detectedCategories.forEach(category ->
                category.setName(this.mapper.getSystemValue(category.getName())));

        List<DetectedCategoryResponse> responses = new ArrayList<>();

        detectedCategories.forEach(object ->
                categoryRepository.findByNameEqualsIgnoreCase(object.getName())
                .ifPresent(partCategory -> responses.add(DetectedCategoryResponse.builder()
                        .categoryId(partCategory.getId())
                        .score(object.getScore())
                        .position(object.getPosition())
                        .build())));

        return responses;
    }
}
