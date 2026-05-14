package cn.com.springCai.bridgeMethod;

/**
 * @Author caiJH
 * @Date 2025/7/1 4:09 PM
 * @Version 1.0
 */
public class BoxStr implements Box<String>{
    @Override
    public String print(String str) {
        System.out.printf("%s\n",str);
        return str;
    }

    @Override
    public void otherMethod(String str) {
        System.out.printf("otherMethodTest\n");
    }

    public static void main(String[] args) {
        BoxStr boxStr = new BoxStr();
        boxStr.print("bridge Method ! ");
    }
}
