# 1brc
Here are some of the details / thought process of the approaches

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

<details>
<summary>V2 - Calculation on the fly</summary>  
<br/>
  
  > Duration: 2m 27s  
  
Instead of storing everything and then start calculating, we take the temperature data, and put it in an object Measurement. First, it is much smaller to maintain an object of 4 double, and 1 int primitive data. Second, the memory complexity is now depends on the number of unique station only. It doesn't depends on the number of row anymore.

**Step Analogy**
1. Parse line → extract station & temperature.
2. Immediately aggregate into a `Measurement` (min, max, sum, count).
3. **Discard the raw temperature value** — it’s no longer in memory.
4. Only keeps 1 `Measurement` object per station.

→ Memory usage stays constant regardless of total rows.
</details>

<details>
<summary>V3 - Better Parsing for double and String split</summary>  
<br/>
  
  > Duration: 1m 34s 

**String Split**
Split is a costly, becuse it is using regex matching. It creates an **array** with **all substrings** — even if you only need two parts.It allocates new `String` objects for every piece, causing more memory churn.
- `split()` involves regex parsing each time, which is quite heavy.
- `indexOf()` is a simple loop scanning for one char — much lighter.
- The fewer temporary objects you create, the less time JVM spends in garbage collection.

**String to Double**
Double.parseDouble() is a general parser to parse string into double, which is too general in this case. In our case, the format can only be like -xx.x or xx.x or -x.x or x.x. So it will be much faster to write a customized Parser for that.

→ I should use byte for even better performance
</details>


<details>
<summary>V4 - Split file to chunks and pass it into multiple threads (WIP)</summary>  
<br/>

The high level concept is that, we split the file into multiple pieces. Each threads take one piece of it, and compute the map of data for their own chunks. After all, loop over the maps of data and merge the data back together.

Multiple things to be considered: 
- split the file into chunks without cutting the line into half
- number of threads, and chunks
- How to merge it back


</details>
