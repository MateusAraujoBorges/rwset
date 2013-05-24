package org.prevayler.tests.demos.demo2.business.transactions;
import java.util.Date;

import org.prevayler.tests.demos.demo2.business.Account;
import org.prevayler.tests.demos.demo2.business.Bank;
abstract class AccountTransaction extends BankTransaction {
  private long _accountNumber;
  AccountTransaction(){
  }
  protected AccountTransaction(  Account account){
    _accountNumber=account.number();
  }
  protected Object executeAndQuery(  Bank bank,  Date timestamp) throws Exception {
    executeAndQuery(bank.findAccount(_accountNumber),timestamp);
    return null;
  }
  protected abstract void executeAndQuery(  Account account,  Date timestamp) throws Exception ;
}
