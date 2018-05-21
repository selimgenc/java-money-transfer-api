import com.revolut.bank.api.JaxrsApp;
import com.revolut.bank.api.account.dto.AccountDto;
import io.reactivex.subjects.PublishSubject;
import org.glassfish.jersey.client.internal.HttpUrlConnector;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

public class AccountResourceTest extends JerseyTest {
    private static final String V1 = "v1";
    private static final String PATH = "account";
    @Test
    public void testGetAccount_NotFound(){
        Response response = target(V1).path(PATH).path("1244").request().get();

        Assert.assertEquals(Response.Status.NOT_FOUND, response.getStatusInfo().toEnum());
    }

    @Test
    public void testCreateAccount_Created(){
        AccountDto accountDto = new AccountDto("slmgnec", BigDecimal.valueOf(50));

        Response response = target(V1).path(PATH).request()
                .header("user","by aa")
                .header("clint","junit")
                .post(Entity.entity(accountDto, MediaType.APPLICATION_JSON_TYPE));

        Assert.assertEquals(Response.Status.CREATED, response.getStatusInfo().toEnum());

        String accountNumber = getAccountNumberFromResponse(response);

        accountDto = target(V1).path(PATH).path(accountNumber).request().get(AccountDto.class);
        Assert.assertEquals(accountNumber, accountDto.getAccountNumber());
    }
    private String getAccountNumberFromResponse(Response response){
        String accountLocation = response.getHeaderString("Location");
        String[] split = accountLocation.split("/");
        return split[split.length - 1];
    }

    @Override
    protected Application configure(){
        return new JaxrsApp();
    }
}
