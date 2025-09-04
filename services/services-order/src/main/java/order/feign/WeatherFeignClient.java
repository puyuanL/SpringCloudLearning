package order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 向第三方 url 发送请求
 */
@FeignClient(value = "weather-client", url = "https://aliv18.data.moji.com")
public interface WeatherFeignClient {
    /**
     * 参数情况从第三方 api 接口文档中获取
     * @param auth 申请的链接标识码
     * @param token 申请的 token
     * @param cityId 城市 id
     */
    @PostMapping("/whapi/json/......")
    String getWeather(@RequestHeader("Authorization") String auth,
                    @RequestParam("token") String token,
                    @RequestParam("cityId") String cityId);
}
