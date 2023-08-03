package testcode.cafekiosk.unit.beverage;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class AmericanoTest {

    @Test
    void getName(){
        Americano americano = new Americano();

        //assertEquals(americano.getName(), "아메리카노");//Junit API

        assertThat(americano.getName()).isEqualTo("아메리카노");//AssertJ

    }

    @Test
    void getPrice(){
        Americano americano = new Americano();

        assertThat(americano.getPrice()).isEqualTo(4000);
    }
}