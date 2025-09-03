public class Blob1 {  
    public static void main(String[] args) throws IOException {  
        String filePath = "./measurements.txt";  
  
        // file read into memory, hashtable  
  
        HashMap<String, List<Double>> stationData = new HashMap<>();  
  
        // parse - separate name and temp by ;  
        try (Stream<String> lines = Files.lines(Path.of(filePath))) {  
            lines.forEach(s -> {  
                String[] listSplit = s.split(";");  
                String stationName = listSplit[0];  
                Double temperature = Double.valueOf(listSplit[1]);  
                List<Double> tempList = stationData.getOrDefault(stationName, new ArrayList<>());  
                tempList.add(temperature);  
                stationData.put(stationName, tempList);  
            });  
        }  
        catch (Exception e) {  
            System.out.println(e.getMessage());  
        }  
  
        System.out.println(stationData.keySet());  
    }  
}
