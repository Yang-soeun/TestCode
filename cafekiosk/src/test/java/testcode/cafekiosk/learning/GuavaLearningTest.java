package testcode.cafekiosk.learning;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.Lists;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GuavaLearningTest {
    @DisplayName("주어진 개수만큼 List를 파티셔닝한다. -3개로 분할")
    @Test
    void partitionLearningTest1(){
        //given
        List<Integer> integers = List.of(1, 2, 3, 4, 5, 6);

        //when
        List<List<Integer>> partition = Lists.partition(integers, 3);

        //then
        assertThat(partition).hasSize(2)
                .isEqualTo(List.of(
                        List.of(1,2,3), List.of(4,5,6)
                ));
    }

    @DisplayName("주어진 개수만큼 List를 파티셔닝한다. -4개로 분할")
    @Test
    void partitionLearningTest2(){
        //given
        List<Integer> integers = List.of(1, 2, 3, 4, 5, 6);

        //when
        List<List<Integer>> partition = Lists.partition(integers, 4);

        //then
        assertThat(partition).hasSize(2)
                .isEqualTo(List.of(
                        List.of(1,2,3,4), List.of(5,6)
                ));
    }
}
