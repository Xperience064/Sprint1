package network;

import model.FakeStoreCartRequest;
import model.FakeStoreCartResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @POST("carts") Call<FakeStoreCartResponse> addToCartApi(@Body FakeStoreCartRequest request);
    @PUT("carts/{id}") Call<FakeStoreCartResponse> updateCartApi(@Path("id") int cartId, @Body FakeStoreCartRequest request);
    @DELETE("carts/{id}") Call<FakeStoreCartResponse> deleteCartApi(@Path("id") int cartId);
}
