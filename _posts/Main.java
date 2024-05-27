
/**
 * 클라이언트용 클래스
 */
public class Main {

    public static void main(String[] args) {
        

    }

}

/**
 * 객체를 생성하기 위한 설계도 
 */
class ExampleClass {
    // 기본 생성자. 아래 처럼 직접 코딩하지 않을 경우 컴파일 시점에 java complier 자동으로 추가해 준다.
    public ExampleClass(){}

    public static ExampleClass newinstance(){
        return new ExampleClass();
    }
}
