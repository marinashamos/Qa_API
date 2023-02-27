package okhttp;

import com.google.gson.Gson;
import dto.AuthRequestDto;
import dto.AuthResponseDto;
import dto.ErrorDto;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class LoginTestsOkhttp {

    private final MediaType JSON = MediaType.get("application/json;charset=utf-8");
    Gson gson = new Gson();
    OkHttpClient client = new OkHttpClient();

    @Test
    public void loginSuccess() throws IOException {

        AuthRequestDto auth = AuthRequestDto.builder().username("dsa@gmail.com").password("Qq12345$").build();

        RequestBody body = RequestBody.create(gson.toJson(auth), JSON);

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/login/usernamepassword")
                .post(body).build();

        Response response = client.newCall(request).execute();

        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.code(), 200);

        AuthResponseDto resDto = gson.fromJson(response.body().string(), AuthResponseDto.class);
        System.out.println(resDto.getToken());
}


//eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoiZHNhQGdtYWlsLmNvbSIsImlzcyI6IlJlZ3VsYWl0IiwiZXhwIjoxNjc4MTI4NzEwLCJpYXQiOjE2Nzc1Mjg3MTB9.mr0YIOCfELBpvmpjbNi4xMs5FU7R6MBx3fnWZbxW8gU

    @Test
    public void loginWrongEmail() throws IOException {
        AuthRequestDto auth = AuthRequestDto.builder().username("dsagmail.com").password("Qq12345$").build();
        RequestBody body = RequestBody.create(gson.toJson(auth), JSON);

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/login/usernamepassword")
                .post(body).build();

        Response response = client.newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(), 401);

        ErrorDto errorDto = gson.fromJson(response.body().string(), ErrorDto.class);

        Assert.assertEquals(errorDto.getStatus(), 401);
        Assert.assertEquals(errorDto.getError(), "Unauthorized");
        Assert.assertEquals(errorDto.getMessage(), "Login or Password incorrect");
    }
}