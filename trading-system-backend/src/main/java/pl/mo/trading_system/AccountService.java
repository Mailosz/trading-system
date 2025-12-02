package pl.mo.trading_system;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service

public class AccountService {

    final long accountId;

    public AccountService(@Value("${current.account.id}") long accountId) {
        this.accountId = accountId;
    }

    public long getCurrentAccountId() {
        return accountId;
    }
}
