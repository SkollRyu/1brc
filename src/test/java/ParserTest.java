/*
 *  Copyright 2023 The original authors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import dev.morling.onebrc.CalculateAverage_SkollRyu;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class ParserTest {

    static Stream<Arguments> provideParseInputs(){
        return Stream.of(
                Arguments.of("12.5", 12.5),
                Arguments.of("-12.5", -12.5),
                Arguments.of("0.5", 0.5),
                Arguments.of("-0.5", -0.5)
                );
    }

    @ParameterizedTest
    @MethodSource("provideParseInputs")
    void test1(String input, double expected){
        double actual = CalculateAverage_SkollRyu.Parser.parse(input);
        Assertions.assertEquals(expected, actual);
    }
}
