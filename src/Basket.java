import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Basket {
    protected int[] amount;
    protected String[] products;
    protected int[] price;
    protected int total;
    Map<Integer, Integer> list = new HashMap<>();

    public Basket(String[] products, int[] price) {
        this.products = products;
        this.price = price;
    }

    public Basket(String[] products, int[] price, int[] amount) {
        this.products = products;
        this.price = price;
        this.amount = amount;
        for (int i = 0; i < products.length; i++) {
            addToCart(i, amount[i]);
        }
    }

    static Basket loadFromTxtFile(File textFile, String[] products) {
        String[] productList = new String[products.length];
        int[] priceList = new int[products.length];
        int[] amountList = new int[products.length];
        try (BufferedReader br = new BufferedReader(new FileReader(textFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] basket = line.split(" ");
                productList[Integer.parseInt(basket[0])] = basket[1];
                priceList[Integer.parseInt(basket[0])] = Integer.parseInt(basket[2]);
                amountList[Integer.parseInt(basket[0])] = Integer.parseInt(basket[3]);
            }
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
        return new Basket(productList, priceList, amountList);
    }

    public void addToCart(int productNum, int amount) {
        productNum = getProductNum(productNum);
        amount = getAmount(amount);
        if (productNum == -1) {
            System.out.println("Неправильный ввод позиции товара!");
            productNum = 0;
            amount = 0;
        }
        if (list.containsKey(productNum)) {
            list.put(productNum, list.get(productNum) + amount);
        }
        if (!list.containsKey(productNum)) {
            list.put(productNum, amount);
        }
    }

    int getProductNum(int productNum) {
        return (productNum < 0 || productNum > products.length - 1) ? -1 : productNum;
    }

    int getAmount(int amount) {
        if (amount < 0) {
            System.out.println("Количество товара не  может быть отрицательным!");
        }
        return Math.max(amount, 0);
    }

    public void printCart() {
        System.out.println("Ваша корзина:");
        for (Integer i : list.keySet()) {
            if (list.get(i) != 0) {
                System.out.println(products[i] + ": " + list.get(i) + " шт на сумму: " + (list.get(i) * price[i]) + " руб");
                this.total = total + (list.get(i) * price[i]);
            }
        }
        System.out.println("Итого: " + this.total);
    }

    public void printBasket() {
        System.out.println("В вашей корзине уже находится: ");
        for (int i = 0; i < products.length; i++) {
            if (amount[i] != 0) {
                System.out.println(products[i] + ": " + amount[i] + " шт.");
            }
        }
        System.out.println("Желаете добавить что-то из списка:");
    }

    public void saveTxt(File textFile) {
        try (PrintWriter printList = new PrintWriter(textFile)) {
            for (int i = 0; i < products.length; i++) {
                if (list.get(i) == null) {
                    printList.println(i + " " + products[i] + " " + price[i] + " " + "0");
                } else {
                    printList.println(i + " " + products[i] + " " + price[i] + " " + list.get(i));
                }
            }
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }
}
