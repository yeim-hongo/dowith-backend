package imhong.dowith.common;

public class ResponseHeaderHelper {

    public static Long getResourceIdFromLocation(String location) {
        return Long.valueOf(location.substring(location.lastIndexOf("/") + 1));
    }
}
