import com.revolut.bank.api.JaxrsApp;
import com.revolut.bank.api.exception.ApiErrorMessage;
import com.revolut.bank.api.transaction.dto.TransactionDto;
import com.revolut.bank.application.AccountService;
import com.revolut.bank.application.internal.AccountServiceImpl;
import com.revolut.bank.domain.account.Account;
import com.revolut.bank.domain.account.AccountRepository;
import com.revolut.bank.domain.handling.DomainException;
import com.revolut.bank.infrustructure.persistance.AccountRepositoryInMem;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.UUID;

public class TransactionResourceTest extends JerseyTest {
    private static final String PATH= "transaction";
    private static final String V1 = "v1";

    @Test
    public void test_Successful_Transaction() throws DomainException {
        //Create Accounts
        String accountNumber1= UUID.randomUUID().toString();
        String accountNumber2= UUID.randomUUID().toString();

        AccountRepositoryInMem accountRepository = new AccountRepositoryInMem();
        accountRepository.store(new Account(accountNumber1, new BigDecimal(100), "slm"));
        accountRepository.store(new Account(accountNumber2, new BigDecimal(100), "slm"));

        //create Ttansfer object
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setAmount(BigDecimal.TEN);
        transactionDto.setFrom(accountNumber1);
        transactionDto.setTo(accountNumber2);

        Response response = target(V1).path(PATH).request()
                .header("location","MOBILE")
                .header("user","customer 1")
                .header("clint","junit")
                .post(Entity.entity(transactionDto, MediaType.APPLICATION_JSON_TYPE));

        Assert.assertEquals(Response.Status.CREATED, response.getStatusInfo().toEnum());

        String transactionNumber = getTransactionNumberFromResponse(response);

        transactionDto = target(V1).path(PATH).path(transactionNumber).request().get(TransactionDto.class);


        Assert.assertEquals(transactionNumber, transactionDto.getTransactionNumber());

    }


    @Test
    public void test_NonExisingTransaction_Should_Return_404(){
        String randomID = UUID.randomUUID().toString();
        Response response = target(V1).path(PATH).path(randomID).request().get();

        Assert.assertEquals(Response.Status.NOT_FOUND, response.getStatusInfo().toEnum());
    }

    @Test
    public void test_NonExistingAccount_Should_Return_Internal_Error(){ //Test one domain exception
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setAmount(BigDecimal.TEN);
        transactionDto.setFrom(UUID.randomUUID().toString());
        transactionDto.setTo(UUID.randomUUID().toString());

        Response response = target(V1).path(PATH).request()
                .header("location","MOBILE")
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

    @Override
    protected Application configure(){
        return new JaxrsApp();
    }
}
