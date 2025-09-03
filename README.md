# 1brc

<details>
<summary>V1 - My initial Approach</summary>  
<br/>

  > Duration: Out-of-memory
  
This initial approach to load all the data line by line into the hashmap with Station Name as key, and List of Temperature value. However, this always Out of memory issue, no matter how large the java heap size I allocate to it.

Why this is memory inefficient?
- Double Wrapper Class is around 24 bytes, where primitive double is 8 bytes
- ArrayList use 8 bytes to reference each Double
- So for one station, if we have 10M temperature data, it will be (24+8) * 10,000,000 = 320MB per station
- If we have 10 stations, it will be 3200MB, around 3GB ram already.
- In our case, we have 1 billion row of data, (24+8) * 1,000,000,000 = 29.80 GiB for raw. Need to upscale +50%.

**Step Analogy**
1. Parse line → extract station & temperature.
2. Box temperature into a `Double` object.
3. Store that `Double` in a `List<Double>` for that station.
4. **Never discards** those values until the very end.

→ Memory usage grows linearly with the number of rows processed
</details>

