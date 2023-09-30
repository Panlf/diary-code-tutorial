package com.plf.diary;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.query.FluxTable;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @author panlf
 * @date 2023/9/30
 */
public class InfluxDbTutorial {

    final String token = "SrHt3OkVsd11w-MniFmZHI9UTlMI5SyfzZHYyv9hrv8_zJUjCdBe1Rdhx6dIBPDNZ7AqYt89nrPPe06VB9F84Q==";
    @Test
    public void testConn(){
        InfluxDBClient influxDBClient = InfluxDBClientFactory.create("http://localhost:8086",
                token.toCharArray(),
                "panlf","history");
        List<FluxTable> twoMinData = influxDBClient.getQueryApi().query("from(bucket:\"history\") |> range(start:-2m)");
        System.out.println(twoMinData);
    }
}
