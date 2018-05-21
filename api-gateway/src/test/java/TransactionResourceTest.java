import com.revolut.bank.api.JaxrsApp;
import com.revolut.bank.api.account.dto.AccountDto;
import com.revolut.bank.api.transaction.dto.TransactionDto;
import com.revolut.bank.domain.handling.DomainException;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.UUID;

public class TransactionResourceTest extends JerseyTest {
    private static final String PATH= "transaction";
    private static final String PATH_ACCOUNT= "account";
    private static final String V1 = "v1";

    @Test
    public void test_Successful_Transaction() throws DomainException {
        //Create Accounts
        AccountDto account1 = createAccountForTransfer("Selim", BigDecimal.valueOf(100));
        AccountDto account2 = createAccountForTransfer("Melisa", BigDecimal.valueOf(200));

        //create Ttansfer object
        TransactionDto transactionDto = new TransactionDto(account1.getAccountNumber(), account2.getAccountNumber(), BigDecimal.TEN);

        Response response = target(V1).path(PATH).request()
                .header("user","customer 1")
                .header("clint","junit")
                .post(Entity.entity(transactionDto, MediaType.APPLICATION_JSON_TYPE));

        Assert.assertEquals(Response.Status.CREATED, response.getStatusInfo().toEnum());

        String transactionNumber = getTransactionNumberFromResponse(response);

        TransactionDto transactionDtoResponse = target(V1).path(PATH).path(transactionNumber).request().get(TransactionDto.class);
        Assert.assertEquals(transactionNumber, transactionDtoResponse.getTransactionNumber());

    }

    @Test
    public void test_NonExisingTransaction_Should_Return_404(){
        String randomID = UUID.randomUUID().toString();
        Response response = target(V1).path(PATH).path(randomID).request().get();

        Assert.assertEquals(Response.Status.NOT_FOUND, response.getStatusInfo().toEnum());
    }

    @Test
    public void test_NonExistingAccount_Should_Return_Internal_Error(){ //Test one domain exception
        TransactionDto transactionDto = new TransactionDto(UUID.randomUUID().toString(), UUID.randomUUID().toString(), BigDecimal.TEN);
        Response response = target(V1).path(PATH).request()
                .header("user","owner from")

                .post(Entity.entity(transactionDto, MediaType.APPLICATION_JSON_TYPE));

        Assert.assertEquals(Response.Status.INTERNAL_SERVER_ERROR, response.getStatusInfo().toEnum());
        Object entity = response.getEntity();
        System.out.println(entity);
    }


    private String getTransactionNumberFromResponse(Response response){
        String accountLocation = response.getHeaderString("Location");
        String[] split = accountLocation.split("/");
        return split[split.length - 1];
    }


    private AccountDto createAccountForTransfer(String customer, BigDecimal credit){
        AccountDto accountDto = new AccountDto(customer, credit);

        Response response = target(V1).path(PATH_ACCOUNT).request()
                .header("user", "by aa")
                .header("clint", "junit")
                .post(Entity.entity(accountDto, MediaType.APPLICATION_JSON_TYPE));

        String accountLocation = response.getHeaderString("Location");
        String[] split = accountLocation.split("/");
        String accountNumber = split[split.length - 1];
        accountDto.setAccountNumber(accountNumber);
        return accountDto;
    }


    @Override
    protected Application configure(){
        return new JaxrsApp();
    }
}
