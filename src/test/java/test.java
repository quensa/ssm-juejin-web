import com.lf.utils.MD5Utils;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

public class test {

    @Test
    public static void main(String[] args) {
        String password = "123456";
        String s = MD5Utils.encodeByMD5(password);
        System.out.println(s);
        String s1 = MD5Utils.encodeByMD5(s);
        System.out.println(s1);
    }
}
