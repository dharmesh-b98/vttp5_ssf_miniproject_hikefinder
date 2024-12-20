package vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.repo;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.constants.*;

@Repository
public class HashRepo {
    @Autowired
    @Qualifier(Constants.template02)
    private RedisTemplate<String, String> template;


    public void put(String key, String hashKey, String value){
        template.opsForHash().put(key, hashKey, value);
    }

    public Object get(String key, String hashKey){
        return template.opsForHash().get(key,hashKey);
    }

    public Boolean hasKey(String key, String hashKey){
        return template.opsForHash().hasKey(key, hashKey);
    }

    public long delete(String key, String hashKey){
        return template.opsForHash().delete(key, hashKey);
    }
    
    public long size(String key){
        return template.opsForHash().size(key);
    }

    public Set<String> keys(String key){
        Set<Object> objectKeys = template.opsForHash().keys(key);
        Set<String> StringKeys = objectKeys.stream().map(e -> (String)e).collect(Collectors.toSet());
        return StringKeys;
    }

    public List<String> values(String key){
        List<Object> objectValues = template.opsForHash().values(key);
        List<String> StringValues = objectValues.stream().map(e -> (String)e).collect(Collectors.toList());
        return StringValues;
    }

    public Map<String, String> entries (String key){
        Map<Object, Object> ObjectEntries = template.opsForHash().entries(key);
        Map<String, String> StringEntries = new HashMap<>();
        for (Map.Entry<Object, Object> entry : ObjectEntries.entrySet()){
            String stringKey = (String) entry.getKey();
            String stringValue = (String) entry.getValue();
            StringEntries.put(stringKey,stringValue);
        }
        return StringEntries;
    }

    public void expire(String key, Integer expirevalue){
        //template.expire(key, expirevalue, TimeUnit.SECONDS);
        Duration expireDuration = Duration.ofSeconds(expirevalue);
        template.expire(key, expireDuration);
    }
}
