import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/*
 * 解码使用x=255-x 编码的字节码文件, 并加载为类.
 * java版本不能低于字节码版本.
 * 该程序使用JDK11 编写.
 */
public class HelloClassLoaderMain{
    // 字节码版本是8, 可以使用java11程序加载.
    private static Path classPath = Paths.get("firstJob","resources", "Hello.xlass");
    private static String classFullName = "Hello";

    public static void main(String[] args) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Class<?> helloClass = new HelloClassLoader(classPath).loadClass(classFullName);
        helloClass.getMethod("hello").invoke(helloClass.getConstructor().newInstance());
    }



}

class HelloClassLoader extends ClassLoader{
    private Path path;

    public HelloClassLoader(Path path){
        this.path = path;
    }
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try{
            byte[] bytes = Files.readAllBytes(path);
            decode(bytes);
            return defineClass(name, bytes, 0, bytes.length);
        } catch (IOException e) {
            throw new ClassNotFoundException(name, e);
        }
    }
    /*
    将bytes中从从from 到to 的byte 恢复为原字节码
    包含from, 不包含to
     */
    private static void decode(byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte)(0xff ^ bytes[i]);
        }
    }
}