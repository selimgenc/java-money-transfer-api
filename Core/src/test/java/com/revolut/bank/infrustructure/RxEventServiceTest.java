package com.revolut.bank.infrustructure;

import com.revolut.bank.application.events.account.AccountCreatedEvent;
import com.revolut.bank.domain.account.Account;
import com.revolut.bank.infrustructure.handling.RxEventServiceImpl;
import com.revolut.bank.infrustructure.handling.handlers.DemoEventConsumer;
import com.revolut.bank.infrustructure.handling.handlers.NotificationAccountsEventConsumer;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Date;

public class RxEventServiceTest {
    @Test
    public void testSubscribePublishAndUnSubscribe(){
        RxEventServiceImpl service = new RxEventServiceImpl();
        DemoEventConsumer demoEventConsumer = new DemoEventConsumer();
        NotificationAccountsEventConsumer accountsEventConsumer = new NotificationAccountsEventConsumer();

        service.register(AccountCreatedEvent.class, accountsEventConsumer);
        service.register(AccountCreatedEvent.class, demoEventConsumer);

        Account account = new Account("ACC-01", new BigDecimal(22), "slmgnc");
        AccountCreatedEvent event = new AccountCreatedEvent(account, new Date());
        service.send(event);

    }

}
