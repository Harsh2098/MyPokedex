package com.hmproductions.mypokedex;

import com.hmproductions.mypokedex.objects.Pokemon;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by harsh on 9/3/17.
 */

public interface PokeApiClient {

    @GET("{name}")
    Call<Pokemon> getPokemonDetails(@Path("name") String name);

}
