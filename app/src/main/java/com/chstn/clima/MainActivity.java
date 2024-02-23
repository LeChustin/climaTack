package com.chstn.clima;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // Definir variables
    double latitud = -0.12429147285586382; // Aeropuerto Internacional Mariscal Sucre(EC)
    double longitud = -78.36058717579186; // Aeropuerto Internacional Mariscal Sucre(EC)
    String API_KEY = "bd5e378503939ddaee76f12ad7a97608";
    String seccion = "dt/main/weather/clouds/wind/visibility/pop/sys";
    String dt_txt = "2023-11-14 12:00:00";

    // Crear la URL de la API
    String url = "https://api.openweathermap.org/data/2.5/forecast?"
            + "lat=" + latitud
            + "&lon=" + longitud
            + "&appid=" + API_KEY
            + "&section=" + seccion
            + "&dt_txt=" + dt_txt;

    // Configurar Retrofit
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    // Crear la interfaz del servicio
    public interface OpenWeatherMapService {

        @GET("forecast")
        Call<WeatherResponse> getForecast(@Query("lat") double latitud, @Query("lon") double longitud, @Query("appid") String apiKey, @Query("section") String seccion, @Query("dt_txt") String dt_txt);
    }

    // Obtener la instancia del servicio
    OpenWeatherMapService service = retrofit.create(OpenWeatherMapService.class);

    // Realizar la llamada a la API
    Call<WeatherResponse> call = service.getForecast(latitud, longitud, API_KEY, seccion, dt_txt);

// Manejar la respuesta
call.enqueue(new Callback<WeatherResponse>() {
        @Override
        public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
            if (response.isSuccessful()) {
                // Procesar la respuesta exitosa
                WeatherResponse weatherResponse = response.body();

                // Obtener la información del clima
                String ciudad = weatherResponse.getName();
                double temperatura = weatherResponse.getMain().getTemp();
                String descripcionClima = weatherResponse.getWeather().get(0).getDescription();
                // ...

                // Obtener la sección y dt_txt
                String seccionObtenida = weatherResponse.getSection();
                String dt_txtObtenido = weatherResponse.getDt_txt();

                // Insertar en SQLite
                // ...

                // Generar notificación
                // ...
            } else {
                // Manejar el error
                Log.e("TAG", "Error al obtener el clima: " + response.code());
            }
        }

        @Override
        public void onFailure(Call<WeatherResponse> call, Throwable t) {
            // Manejar el error de red
            Log.e("TAG", "Error al obtener el clima: " + t.getMessage());
        }
    });


}