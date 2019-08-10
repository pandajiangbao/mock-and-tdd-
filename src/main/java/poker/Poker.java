package poker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Poker {
    private String type;
    private String number;
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Poker(String type, String number) {
        this.type = type;
        this.number = number;
    }

    private static List<String> sizeList= Arrays.asList("2","3","4","5","6","7","8","9","T","J","Q","K","A");

    public static String compareCards(List<Poker> player1,List<Poker> player2){
        List<Integer> cardList1 = player1.stream()
                .map(item -> sizeList.indexOf(item.getNumber()))
                .collect(Collectors.toList());
        List<Integer> cardList2 = player2.stream()
                .map(item -> sizeList.indexOf(item.getNumber()))
                .collect(Collectors.toList());
        return compareCardsNumber(cardList1,cardList2);
    }
    public static String compareCardsNumber(List<Integer> cardList1,List<Integer> cardList2){
        Integer max1=cardList1.stream().max(Comparator.comparing(Integer::valueOf)).get();
        Integer max2=cardList2.stream().max(Comparator.comparing(Integer::valueOf)).get();
        if (max1>max2){
          return  "player1 win";
        }
        else if(max1.equals(max2)){
            cardList1.remove(max1);
            cardList2.remove(max2);
            return compareCardsNumber(cardList1,cardList2);
        }
        return "player2 win";
    }
}
