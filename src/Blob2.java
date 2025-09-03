public class Blob2 {  
  
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
            computeMean();  
        }  
  
        void computeMean() {  
            double scale = Math.pow(10, 1);  
            mean = Math.round((sum / count) * scale) / scale;  
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
                String[] listSplit = s.split(";");  
                String stationName = listSplit[0];  
                double temperature = Double.parseDouble(listSplit[1]);  
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
            sb.append(entrySet.getKey())  
                    .append("=")  
                    .append(entrySet.getValue().toString())  
                    .append(", ");  
        }  
        sb.append("}");  
  
        System.out.println(sb);  
    }  
}
