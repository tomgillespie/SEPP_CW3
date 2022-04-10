package external;

import java.util.ArrayList;
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
        public String getBuyerAccountEmail(){
            return buyerAccountEmail;
        }
        public String getSellerAccountEmail(){
            return sellerAccountEmail;
        }
        public double getTransactionAmount(){
            return transactionAmount;
        }
    }

    public MockPaymentSystem(){
        this.transactionList = new ArrayList<>();
    }

    @Override
    public boolean processPayment(String buyerAccountEmail, String sellerAccountEmail, double transactionAmount) {
        // Catches cases where inputs are invalid
        if (buyerAccountEmail == null || sellerAccountEmail == null || transactionAmount <= 0){
            return false;
        }
        if (buyerAccountEmail.equals(sellerAccountEmail)){
            return false;
        }
        else {
        Transaction latestTransaction = new Transaction(buyerAccountEmail, sellerAccountEmail, transactionAmount);
        this.transactionList.add(latestTransaction);
        return true;
        }
    }

    @Override
    public boolean processRefund(String buyerAccountEmail, String sellerAccountEmail, double transactionAmount) {
        // Remove transaction if refund requested
        for (int i = 0; i < transactionList.size(); i++){
            if (transactionList.get(i).getBuyerAccountEmail().equals(buyerAccountEmail)
                    && transactionList.get(i).getSellerAccountEmail().equals(sellerAccountEmail)
                    && transactionList.get(i).getTransactionAmount() == transactionAmount ){
                transactionList.remove(i);
                return true;
            }
        }
        return false;
    }
}
