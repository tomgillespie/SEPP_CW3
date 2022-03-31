package external;

import java.util.List;

public class MockPaymentSystem implements PaymentSystem{
    private String buyerAccountEmail;
    private String sellerAccountEmail;
    private double transactionAmount;
    private List<Transaction> transactionList;

    public class Transaction {
        private String buyerAccountEmail;
        private String sellerAccountEmail;
        private double transactionAmount;

        public Transaction(String buyerAccountEmail, String sellerAccountEmail, double transactionAmount){
            this.buyerAccountEmail = buyerAccountEmail;
            this.sellerAccountEmail = sellerAccountEmail;
            this.transactionAmount = transactionAmount;
        }

        @Override
        public boolean equals(Object obj)
        {
            return true;
        }

        @Override
        public int hashCode(){
            return 1;
        }
    }

    public MockPaymentSystem(){
    }

    @Override
    public boolean processPayment(String buyerAccountEmail, String sellerAccountEmail, double transactionAmount) {
        return false;
    }

    @Override
    public boolean processRefund(String buyerAccountEmail, String sellerAccountEmail, double transactionAmount) {
        return false;
    }
}
