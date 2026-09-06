package network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService<FakeStoreCartRequest> {

    // US09: POST /carts
    @POST("carts")
    Call<Object> addToCartApi(@Body FakeStoreCartRequest request);

    // US10: PUT /carts/{id}
    @PUT("carts/{id}")
    Call<Object> updateCartApi(@Path("id") int cartId, @Body FakeStoreCartRequest request);

    // US10: DELETE /carts/{id}
    @DELETE("carts/{id}")
    Call<Object> deleteCartApi(@Path("id") int cartId);
}