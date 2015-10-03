package utils;

import com.netflix.config.DynamicPropertyFactory;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

public class Features {

    public static String getCloudSearchDocumentEndpoint() {
        return getStringProperty("cloudsearch.document.endpoint","");
    }

    public static String getCloudSearchSearchEndpoint() {
        return getStringProperty("cloudsearch.search.endpoint","");
    }

    public static int getIntProperty(String key, int defaultValue) {
        return DynamicPropertyFactory.getInstance().getIntProperty(key, defaultValue).get();
    }

    public static String getStringProperty(String key, String defaultValue) {
        return DynamicPropertyFactory.getInstance().getStringProperty(key, defaultValue).get();
    }

    public static Boolean getBooleanProperty(String key, Boolean defaultValue) {
        return DynamicPropertyFactory.getInstance().getBooleanProperty(key, defaultValue).get();
    }

    public static float getFloatProperty(String key, float defaultValue) {
        return DynamicPropertyFactory.getInstance().getFloatProperty(key, defaultValue).get();
    }

}
