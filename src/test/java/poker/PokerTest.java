package poker;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PokerTest {
    @Test
    public void testPoker_given_two_card_list_then_compare_one_card_size(){
        List<Poker> player1 = Arrays.asList(new Poker("C","3"), new Poker("D","4"), new Poker("H","K"), new Poker("H","8"), new Poker("S","5"));
        List<Poker> player2 = Arrays.asList(new Poker("C","A"), new Poker("D","5"), new Poker("H","3"), new Poker("H","4"), new Poker("H","9"));


        String result= Poker.compareCards(player1,player2);

        Assert.assertEquals("player2 win",result);
    }
}
