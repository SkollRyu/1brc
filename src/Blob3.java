public class Blob3 {

    final static String filePath = "./measurements.txt";

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
