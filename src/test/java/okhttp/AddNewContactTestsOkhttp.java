package okhttp;

import com.google.gson.Gson;
import dto.ContactDto;
import dto.ContactResponseDto;
import dto.ErrorDto;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Random;

public class AddNewContactTestsOkhttp {

    Gson gson = new Gson();
    OkHttpClient client = new OkHttpClient();
    String token ="eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoiZHNhQGdtYWlsLmNvbSIsImlzcyI6IlJlZ3VsYWl0IiwiZXhwIjoxNjc4MTI4NzEwLCJpYXQiOjE2Nzc1Mjg3MTB9.mr0YIOCfELBpvmpjbNi4xMs5FU7R6MBx3fnWZbxW8gU";

    private final MediaType JSON = MediaType.get("application/json;charset=utf-8");

    @Test
    public void addNewContactSuccess() throws IOException {
        int i = new Random().nextInt(1000) + 1000;
        ContactDto dto = ContactDto.builder()
                .name("Maria")
                .lastName("Bas")
                .email("maria"+i+"@mail.com")
                .phone("12198765343477" + i)
                .address("NY")
                .description("Friend").build();

        RequestBody body = RequestBody.create(gson.toJson(dto), JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .addHeader("Authorization", token)
                .post(body).build();
        Response response = client.newCall(request).execute();

        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.code(), 200);
        ContactResponseDto resdto = gson.fromJson(response.body().string(), ContactResponseDto.class);
        System.out.println(resdto.getMessage());
        Assert.assertTrue(resdto.getMessage().contains("Contact was added"));
    }

    @Test
    public void addNewContactWrongName() throws IOException {
        ContactDto dto = ContactDto.builder()
                .lastName("Bas")
                .email("maria@mail.com")
                .phone("12198765343477")
                .address("NY")
                .description("Friend").build();

        RequestBody body = RequestBody.create(gson.toJson(dto), JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .addHeader("Authorization", token)
                .post(body).build();
        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(), 400);

        ErrorDto errorDto = gson.fromJson(response.body().string(), ErrorDto.class);
        System.out.println(errorDto.getMessage().toString());
        Assert.assertEquals(errorDto.getMessage().toString(),"{name=must not be blank}");


    }

    @Test
    public void addNewContactWrongEmail() throws IOException {
        ContactDto dto = ContactDto.builder()
                .name("Maria")
                .lastName("Bas")
                .email("mariamail.com")
                .phone("12198765343477")
                .address("NY")
                .description("Friend").build();

        RequestBody body = RequestBody.create(gson.toJson(dto), JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .addHeader("Authorization", token)
                .post(body).build();
        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(), 400);

        ErrorDto errorDto = gson.fromJson(response.body().string(), ErrorDto.class);
        System.out.println(errorDto.getMessage().toString());
        Assert.assertEquals(errorDto.getMessage().toString(),"{email=must be a well-formed email address}");


    }
}
