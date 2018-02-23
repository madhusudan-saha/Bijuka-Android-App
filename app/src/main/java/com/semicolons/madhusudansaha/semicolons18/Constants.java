package com.semicolons.madhusudansaha.semicolons18;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by madhusudan_saha on 23-02-2018.
 */

public final class Constants {
    public static final Map<String, Integer> LANGUAGE;
    public static final Map<Integer, List<String>> TRANSLATION;
    static {
        Map<String, Integer> langMap = new HashMap<String, Integer>();

        langMap.put("English", 0);
        langMap.put("हिंदी", 1);
        langMap.put("मराठी", 2);

        LANGUAGE = Collections.unmodifiableMap(langMap);

        Map<Integer, List<String>> map = new HashMap<Integer, List<String>>();
        //Main Activity
        map.put(R.string.app_name, Arrays.asList("BIJUKA", "बिजूका", "बागुलबुवा"));
        map.put(R.string.subscribe, Arrays.asList("Subscribe For Notification", "अधिसूचना के लिए सदस्यता लें", "सूचना साठी सदस्यता घ्या"));
        map.put(R.string.unsubscribe, Arrays.asList("Unsubscribe For Notification", "अधिसूचना के लिए सदस्यता समाप्त करें", "अधिसूचना रद्द करा"));

        //Health Monitor Activity
        map.put(R.string.checkLightStatus, Arrays.asList("Check Light Status", "लाइट स्टेटस की जांच करें", "लाईट स्थिती तपासा"));
        map.put(R.string.checkLightStatus, Arrays.asList("Check Sound Status", "ध्वनि स्थिति जांचें", "ध्वनी स्थिती तपासा"));
        map.put(R.string.checkLightStatus, Arrays.asList("Check Battery Status", "बैटरी स्थिति जांचें", "बॅटरी स्थिती तपासा"));

        TRANSLATION = Collections.unmodifiableMap(map);
    }
}
