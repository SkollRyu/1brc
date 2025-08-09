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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

public class CalculateAverage_SkollRyu {

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
            mean = sum / count;
        }
    }

    public static void main(String[] args) throws IOException {
        String filePath = "./measurements.txt";

        // file read into memory, hashtable

        HashMap<String, Measurement> stationData = new HashMap<>();

        // parse - separate name and temp by ;
        try (Stream<String> lines = Files.lines(Path.of(filePath))) {
            lines.forEach(s -> {
                String[] listSplit = s.split(";");
                String stationName = listSplit[0];
                double temperature = Double.parseDouble(listSplit[1]);
                Measurement measurement = stationData.getOrDefault(stationName, new Measurement());
                measurement.add(temperature);
                stationData.put(stationName, measurement);
            });
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println(stationData);
    }
}
