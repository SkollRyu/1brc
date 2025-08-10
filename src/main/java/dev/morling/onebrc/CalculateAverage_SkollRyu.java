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

package dev.morling.onebrc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.TreeMap;
import java.util.stream.Stream;

public class CalculateAverage_SkollRyu {

    public static class Parser {
        ;

        public static double parse(String s) {
            char[] charArray = s.toCharArray();
            boolean isNegative = charArray[0] == '-';
            int decimalDigit = charArray[charArray.length - 1] - '0';
            int firstDigit;
            int secondDigit;

            if (isNegative) {
                firstDigit = charArray[1] - '0';
                secondDigit = charArray.length == 5 ? charArray[2] - '0' : -1;
                if (secondDigit == -1) {
                    return -1 * (firstDigit + (double) decimalDigit / 10);
                }
                else {
                    return -1 * (firstDigit * 10 + secondDigit + (double) decimalDigit / 10);
                }

            }
            else {
                firstDigit = charArray[0] - '0';
                secondDigit = charArray.length == 4 ? charArray[1] - '0' : -1;
                if (secondDigit == -1) {
                    return (firstDigit + (double) decimalDigit / 10);
                }
                else {
                    return (firstDigit * 10 + secondDigit + (double) decimalDigit / 10);
                }
            }
        }
    }

    static class Measurement {
        public double min = Double.MAX_VALUE;
        public double max = Double.MIN_VALUE;
        public double sum = 0;
        public int count = 0;
        public double mean;

        void add(double newTemp) {
            min = Math.min(newTemp, min);
            max = Math.max(newTemp, max);
            sum += newTemp;
            count++;
        }

        void computeMean() {
            mean = Math.round((sum / count) * 10.0) / 10.0;
        }

        @Override
        public String toString() {
            return min +
                    "/" + max +
                    "/" + mean;
        }
    }

    public static void main(String[] args) throws IOException {
        String filePath = "./measurements.txt";

        // file read into memory, hashtable
        TreeMap<String, Measurement> stationData = new TreeMap<>();

        // parse - separate name and temp by ;
        try (Stream<String> lines = Files.lines(Path.of(filePath))) {
            lines.forEach(s -> {
                int splitIndex = s.indexOf(";");
                String stationName = s.substring(0, splitIndex);
                double temperature = Parser.parse(s.substring(splitIndex + 1));
                stationData
                        .computeIfAbsent(stationName, k -> new Measurement())
                        .add(temperature);
            });
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (var entrySet : stationData.entrySet()) {
            entrySet.getValue().computeMean();
            sb.append(entrySet.getKey())
                    .append("=")
                    .append(entrySet.getValue().toString())
                    .append(", ");
        }
        sb.append("}");

        System.out.println(sb);
    }
}
