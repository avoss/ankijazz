package de.jlab.scales.irealb;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.junit.Before;
import org.junit.Test;

public class IRealBTest {
  // https://github.com/ironss/accompaniser/blob/master/test/test_irealb_parser.lua
  private IRealB irealb;

  @Before
  public void setUp() {
    irealb = new IRealB();
  }

  @Test
  public void test_obfusc_050() {
    String tune = "[T44A BLZC DLZE FLZG, ALZA BLZC DLZE FLZG ALZA B | ";
    String expected = "[T44A BLZC DLZE FLZG, ALZA BLZC DLZE FLZG ALZA B | ";
    String actual = irealb.unobfusc(tune);
    assertEquals(expected, actual);
  }

  @Test
  public void test_obfusc_051() {
    String tune = "| B A BLZCZLF EZLD CZLB ZALA ,GZLF EZLD G ALZA44T[C ";
    String expected = "[T44A BLZC DLZE FLZG, ALZA BLZC DLZE FLZG ALZA B |C ";
    String actual = irealb.unobfusc(tune);
    assertEquals(expected, actual);
  }

  @Test
  public void test_obfusc_100() {
    String tune = "ZLB A BLZCZLF EZLD CZLB ZALA ,GZLF EZLD G ALZA44T[C- DLZE FLZG A,LZA BLZC DLZE FLZG ALZA BLZC- D |E ";
    String expected = "[T44A BLZC DLZE FLZG, ALZA BLZC DLZE FLZG ALZA BLZC- DLZE FLZG A,LZA BLZC DLZE FLZG ALZA BLZC- D |E ";
    String actual = irealb.unobfusc(tune);
    assertEquals(expected, actual);
  }

  @Test
  public void test_obfusc_101() {
    String tune = "ZLB A BLZCZLF EZLD CZLB ZALA ,GZLF EZLD G ALZA44T[ EZLDZE FLB AZLA GZLF EZDL CZLB AZL,A GZLZC- LD -CF ";
    String expected = "[T44A BLZC DLZE FLZG, ALZA BLZC DLZE FLZG ALZA BLZC- DLZE FLZG A,LZA BLZC DLZE FLZG ALZA BLZC- DLZE F ";
    String actual = irealb.unobfusc(tune);
    assertEquals(expected, actual);
  }

//  final String songUrlEncoded = "irealb://II-V-I%20in%2012%20Major%20Keys%3DExercise%3D%3DMedium%20Swing%3DC%3D%3D1r34LbKcu7cKQyX-7XyQyX7C%7CQyX7-GZL%20clKQyX7%5EC%7CQyX7G%7CQ%7CF%5E7D44T%5BX7%5EbEC-7XybB%7CQyX7-FZL%20lcQKyX7%5EbB%7CQyX7F%7CQ7XyQ%7C%7CZL%20lQyX7b%20LZBb7-bEZL%20lcKQyX7b%5EA%7CQyX7bE%7CQyX7-XyQ%7CAlcKQyQyX7-XyQKclcKQyX7%5EbG%7CQyXb7D%7CQyX7-bA%7CZL%20l%20LZC%237%5EbD%7CX7-BZyQ%7CB%5EQyX7%5EE%7CQyX7B%7CQXy7-%23FZL%20lcKQyX7Kcl%20LX7%23F%7CX7-AZXyQ%7CAQyX7%5ED%7CQyX7A%7CQXy7-EZL%20lcKQyX7%5EKcl%20L7E%7CQyyQ%7CD7XyQ%7CG%5E7XyQKcl%20%20Z%20%3D%3D0%3D0";
//  final String songUrlDecoded = "irealb://II-V-I in 12 Major Keys=Exercise==Medium Swing=C==1r34LbKcu7cKQyX-7XyQyX7C|QyX7-GZL clKQyX7^C|QyX7G|Q|F^7D44T[X7^bEC-7XybB|QyX7-FZL lcQKyX7^bB|QyX7F|Q7XyQ||ZL lQyX7b LZBb7-bEZL lcKQyX7b^A|QyX7bE|QyX7-XyQ|AlcKQyQyX7-XyQKclcKQyX7^bG|QyXb7D|QyX7-bA|ZL l LZC#7^bD|X7-BZyQ|B^QyX7^E|QyX7B|QXy7-#FZL lcKQyX7Kcl LX7#F|X7-AZXyQ|AQyX7^D|QyX7A|QXy7-EZL lcKQyX7^Kcl L7E|QyyQ|D7XyQ|G^7XyQKcl  Z ==0=0";
//  private String encoded;
//  @Test
//  public void print() {
//    String s =   "cKQyX-7XyQyX7C|QyX7-GZL clKQyX7^C|QyX7G|Q|F^7D44T[X7^bEC-7XybB|QyX7-FZL lcQKyX7^bB|QyX7F|Q7XyQ||ZL lQyX7b LZBb7-bEZL lcKQyX7b^A|QyX7bE|QyX7-XyQ|AlcKQyQyX7-XyQKclcKQyX7^bG|QyXb7D|QyX7-bA|ZL l LZC#7^bD|X7-BZyQ|B^QyX7^E|QyX7B|QXy7-#FZL lcKQyX7Kcl LX7#F|X7-AZXyQ|AQyX7^D|QyX7A|QXy7-EZL lcKQyX7^Kcl L7E|QyyQ|D7XyQ|G^7XyQKcl  Z ";
//    System.out.println(irealb.unobfusc(s));
//  }
  
  @Test
  public void jazz1() throws UnsupportedEncodingException {
    InputStream is = IRealBTest.class.getResourceAsStream("jazz1.irealb");
    String encoded = new BufferedReader(new InputStreamReader(is)).lines().findFirst().orElseThrow(() -> new RuntimeException("not found"));
    String decoded = URLDecoder.decode(encoded, "UTF-8");
    System.out.println(decoded);
    
    //URLDecoder.decode(s)
  }

}
