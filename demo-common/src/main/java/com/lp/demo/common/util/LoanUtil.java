package com.lp.demo.common.util;

import cn.hutool.json.JSONUtil;
import com.lp.demo.common.exception.DisplayableException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lp
 * @date 2022/12/13 10:39
 * @desc
 **/
public class LoanUtil {
    private static final double COMMERCIAL_LOAN_INTEREST_RATE = 4.9 / 100;
    private static final double PROVIDENT_FUND_LOAN_INTEREST_RATE = 3.1 / 100;

    private static final Input input = new Input();
    private static final InputDiff inputDiff = new InputDiff();

    static {
        input.setUnitPrice(BigDecimal.valueOf(15000));
        input.setArea(100.00);
        input.setTotalAmount(input.getUnitPrice().multiply(BigDecimal.valueOf(input.getArea())));
        input.setDownPaymentRatio(DownPaymentRatio._30);
        input.setDownPaymentAmount(input.getTotalAmount().multiply(BigDecimal.valueOf(input.getDownPaymentRatio().getValue())));
        input.setLoanPeriodInYears(LoanPeriodInYears._30YEAR);
        input.setRepaymentType(RepaymentType.INTEREST_FIRST); // 还款类型 ①
        input.setLoanType(LoanType.COMMERCIAL_LOAN); // 贷款类型 ②
        input.setLoanInterestRate(input.getLoanType().getLoanInterestRate()); // 利率

        BigDecimal totalLoanAmount = input.getTotalAmount().subtract(input.getDownPaymentAmount());
        input.setTotalLoanAmount(totalLoanAmount); // 贷款总额
        Map<LoanType, BigDecimal> loanAmount = new ConcurrentHashMap<>(1 << 2);
        loanAmount.put(LoanType.PROVIDENT_FUND_LOAN, BigDecimal.ZERO); // 公积金贷款金额 BigDecimal.valueOf(60_0000) ③
        loanAmount.put(LoanType.COMMERCIAL_LOAN, totalLoanAmount.subtract(loanAmount.get(LoanType.PROVIDENT_FUND_LOAN))); // 商贷金额
        input.setLoanAmount(loanAmount);
        input.setStartDate(LocalDate.now());

        // n年后
        inputDiff.setCurrentUnitPrice(BigDecimal.valueOf(12000));
        inputDiff.setCurrentTotalAmount(inputDiff.getCurrentUnitPrice().multiply(BigDecimal.valueOf(input.getArea())));
        inputDiff.setTransactionTax(BigDecimal.ZERO);
        inputDiff.setOther(BigDecimal.ZERO);
        inputDiff.setEndDate(LocalDate.now().plusYears(3)); // 3年后
        int months = Period.between(input.getStartDate(), inputDiff.getEndDate()).getMonths();
        inputDiff.setFixedDepositRate(DepositRate.getDepositRateByMonth(months));
        inputDiff.setInflationRate(3.5 / 100);

    }

    public static void main(String[] args) {

        // 计算贷款信息
        Output output = calculateLoan(input);
        System.out.println("output = " + JSONUtil.parseObj(output, true, true));

        // 中断贷款后 亏损/盈利情况
        OutputDiff outputDiff = calculateLoanDiff(input, inputDiff);
        System.out.println("outputDiff = " + JSONUtil.parseObj(outputDiff, true, true));

    }

    public static Output calculateLoan(Input in) {
        if (in.getRepaymentType() == RepaymentType.INTEREST_FIRST) {
            return calculateInterestFirstRepaymentPlan(in);
        }
        if (in.getRepaymentType() == RepaymentType.PRINCIPAL_FIRST) {
            return calculatePrincipalFirstRepaymentPlan(in);
        }
        return null;
    }

    /**
     * 等额本息 还款计划
     *
     * @return
     */
    public static Output calculateInterestFirstRepaymentPlan(Input in) {
        List<MonthlyRepayment> monthlyRepaymentAmount = new ArrayList<>();
        LocalDate date = LocalDate.now();

        // 总还款月
        int totalRepaymentYear = in.getLoanPeriodInYears().getValue();
        int totalRepaymentMonth = totalRepaymentYear * 12;

        // 年利率
        Map<LoanType, Double> yearInterestRate = in.getLoanInterestRate();
        Double yearCommercialInterestRate = yearInterestRate.get(LoanType.COMMERCIAL_LOAN);
        Double yearProvidentFundInterestRate = yearInterestRate.get(LoanType.PROVIDENT_FUND_LOAN);

        // 贷款类型
        LoanType loanType = in.getLoanType();
        verifyLoanRate(loanType, yearInterestRate);

        // 月利率
        double monthlyCommercialInterestRate = yearCommercialInterestRate / 12;
        double monthlyProvidentFundInterestRate = yearProvidentFundInterestRate / 12;

        BigDecimal totalLoanAmount = in.getTotalLoanAmount();
        // 月还款总额
        BigDecimal monthlyRepaymentTotalAmount = getMonthlyRepaymentTotalAmountByInterestFirst(in.getLoanAmount(),
                totalRepaymentMonth, loanType, monthlyCommercialInterestRate, monthlyProvidentFundInterestRate, totalLoanAmount);

        // 利息总额计数
        BigDecimal totalInterestAmount = BigDecimal.ZERO;
        for (int i = 1; i <= totalRepaymentMonth; i++) {

            // 月还款利息
            BigDecimal monthlyRepaymentInterest = getMonthlyRepaymentInterestByInterestFirst(in.getLoanAmount(),
                    totalRepaymentMonth, loanType, monthlyCommercialInterestRate, monthlyProvidentFundInterestRate, totalLoanAmount, i);
            totalInterestAmount = totalInterestAmount.add(monthlyRepaymentInterest);

            // 月还款本金
            BigDecimal monthlyRepaymentPrincipal = monthlyRepaymentTotalAmount.subtract(monthlyRepaymentInterest);

            // 每月还款信息
            MonthlyRepayment monthlyRepayment = MonthlyRepayment.builder()
                    .index(i)
                    .date(date.plusMonths(i - 1).toString())
                    .totalAmount(monthlyRepaymentTotalAmount)
                    .principal(monthlyRepaymentPrincipal)
                    .interest(monthlyRepaymentInterest)
                    .build();
            monthlyRepaymentAmount.add(monthlyRepayment);
        }

        return Output.builder()
                .input(LoanStructMapper.INSTANCE.convert2InputVo(in))
                .totalAmountOwed(totalInterestAmount.add(totalLoanAmount))
                .totalInterestAmount(totalInterestAmount)
                .monthlyRepaymentAmount(monthlyRepaymentAmount)
                .build();
    }

    /**
     * 等额本息 月还款总额
     *
     * @param loanAmount
     * @param totalRepaymentMonth
     * @param loanType
     * @param monthlyCommercialInterestRate
     * @param monthlyProvidentFundInterestRate
     * @param totalLoanAmount
     * @return
     */
    private static BigDecimal getMonthlyRepaymentTotalAmountByInterestFirst(Map<LoanType, BigDecimal> loanAmount,
                                                                            int totalRepaymentMonth,
                                                                            LoanType loanType,
                                                                            double monthlyCommercialInterestRate,
                                                                            double monthlyProvidentFundInterestRate,
                                                                            BigDecimal totalLoanAmount) {
        BigDecimal monthlyRepaymentTotalAmount = BigDecimal.ZERO;
        if (loanType == LoanType.COMMERCIAL_LOAN) {
            monthlyRepaymentTotalAmount = getMonthlyRepaymentTotalAmountByLoanType(totalLoanAmount, monthlyCommercialInterestRate, totalRepaymentMonth);
        }
        if (loanType == LoanType.PROVIDENT_FUND_LOAN) {
            monthlyRepaymentTotalAmount = getMonthlyRepaymentTotalAmountByLoanType(totalLoanAmount, monthlyProvidentFundInterestRate, totalRepaymentMonth);
        }
        if (loanType == LoanType.BLENDED_LOAN) {
            BigDecimal commercialLoanAmount = loanAmount.get(LoanType.COMMERCIAL_LOAN);
            BigDecimal providentFundLoanAmount = loanAmount.get(LoanType.PROVIDENT_FUND_LOAN);
            // 月还款总额 = 商贷月还总额 + 公积金月还总额
            BigDecimal monthlyRepaymentCommercialLoanAmount = getMonthlyRepaymentTotalAmountByLoanType(commercialLoanAmount, monthlyCommercialInterestRate, totalRepaymentMonth);
            BigDecimal monthlyRepaymentProvidentFundLoanAmount = getMonthlyRepaymentTotalAmountByLoanType(providentFundLoanAmount, monthlyProvidentFundInterestRate, totalRepaymentMonth);
            monthlyRepaymentTotalAmount = monthlyRepaymentCommercialLoanAmount.add(monthlyRepaymentProvidentFundLoanAmount);
        }
        return monthlyRepaymentTotalAmount;
    }

    /**
     * 等额本息 月还款利息
     *
     * @param loanAmount
     * @param totalRepaymentMonth
     * @param loanType
     * @param monthlyCommercialInterestRate
     * @param monthlyProvidentFundInterestRate
     * @param totalLoanAmount
     * @param index
     * @return
     */
    private static BigDecimal getMonthlyRepaymentInterestByInterestFirst(Map<LoanType, BigDecimal> loanAmount,
                                                                         int totalRepaymentMonth,
                                                                         LoanType loanType,
                                                                         double monthlyCommercialInterestRate,
                                                                         double monthlyProvidentFundInterestRate,
                                                                         BigDecimal totalLoanAmount,
                                                                         int index) {
        BigDecimal monthlyRepaymentInterest = BigDecimal.ZERO;
        if (loanType == LoanType.COMMERCIAL_LOAN) {
            monthlyRepaymentInterest = getMonthlyRepaymentInterestByLoanType(totalLoanAmount, monthlyCommercialInterestRate, totalRepaymentMonth, index);
        }
        if (loanType == LoanType.PROVIDENT_FUND_LOAN) {
            monthlyRepaymentInterest = getMonthlyRepaymentInterestByLoanType(totalLoanAmount, monthlyProvidentFundInterestRate, totalRepaymentMonth, index);
        }
        if (loanType == LoanType.BLENDED_LOAN) {
            BigDecimal commercialLoanAmount = loanAmount.get(LoanType.COMMERCIAL_LOAN);
            BigDecimal providentFundLoanAmount = loanAmount.get(LoanType.PROVIDENT_FUND_LOAN);
            // 月还款利息 = 商贷月还利息 + 公积金月还利息
            BigDecimal monthlyRepaymentCommercialLoanInterest = getMonthlyRepaymentInterestByLoanType(commercialLoanAmount, monthlyCommercialInterestRate, totalRepaymentMonth, index);
            BigDecimal monthlyRepaymentProvidentFundLoanInterest = getMonthlyRepaymentInterestByLoanType(providentFundLoanAmount, monthlyProvidentFundInterestRate, totalRepaymentMonth, index);
            monthlyRepaymentInterest = monthlyRepaymentCommercialLoanInterest.add(monthlyRepaymentProvidentFundLoanInterest);
        }
        return monthlyRepaymentInterest;
    }

    /**
     * 等额本息 月还款利息
     * 公式：每月偿还利息=贷款本金×月利率×〔(1+月利率)^还款月数-(1+月利率)^(还款月序号-1)〕÷〔(1+月利率)^还款月数-1〕
     *
     * @param loanAmount          贷款金额
     * @param monthlyInterestRate 月利率
     * @param totalRepaymentMonth 总还款月
     * @param index               第几月次还款
     * @return
     */
    private static BigDecimal getMonthlyRepaymentInterestByLoanType(BigDecimal loanAmount, double monthlyInterestRate, int totalRepaymentMonth, int index) {
        return loanAmount.multiply(BigDecimal.valueOf(monthlyInterestRate))
                .multiply(BigDecimal.valueOf(Math.pow(1 + monthlyInterestRate, totalRepaymentMonth)).subtract(BigDecimal.valueOf(Math.pow(1 + monthlyInterestRate, index - 1))))
                .divide(BigDecimal.valueOf(Math.pow(1 + monthlyInterestRate, totalRepaymentMonth) - 1), 6, BigDecimal.ROUND_DOWN)
                .setScale(2, BigDecimal.ROUND_DOWN);
    }

    /**
     * 等额本息 月还款总额
     * 公式：每月偿还本息=〔贷款本金×月利率×(1＋月利率)＾还款月数〕÷〔(1＋月利率)＾还款月数-1〕
     *
     * @param loanAmount          贷款金额
     * @param monthlyInterestRate 月利率
     * @param totalRepaymentMonth 总还款月
     * @return
     */
    private static BigDecimal getMonthlyRepaymentTotalAmountByLoanType(BigDecimal loanAmount, double monthlyInterestRate, int totalRepaymentMonth) {
        return loanAmount.multiply(BigDecimal.valueOf(monthlyInterestRate * Math.pow(1 + monthlyInterestRate, totalRepaymentMonth)))
                .divide(BigDecimal.valueOf(Math.pow(1 + monthlyInterestRate, totalRepaymentMonth) - 1), 2, BigDecimal.ROUND_DOWN);
    }

    /**
     * 等额本金还款计划
     *
     * @return
     */
    public static Output calculatePrincipalFirstRepaymentPlan(Input in) {
        List<MonthlyRepayment> monthlyRepaymentAmount = new ArrayList<>();
        LocalDate date = LocalDate.now();

        // 总还款月
        int totalRepaymentYear = in.getLoanPeriodInYears().getValue();
        int totalRepaymentMonth = totalRepaymentYear * 12;

        // 年利率
        Map<LoanType, Double> yearInterestRate = in.getLoanInterestRate();
        Double yearCommercialInterestRate = yearInterestRate.get(LoanType.COMMERCIAL_LOAN);
        Double yearProvidentFundInterestRate = yearInterestRate.get(LoanType.PROVIDENT_FUND_LOAN);

        LoanType loanType = in.getLoanType();
        verifyLoanRate(loanType, yearInterestRate);

        // 月利率
        double monthlyCommercialInterestRate = yearCommercialInterestRate / 12;
        double monthlyProvidentFundInterestRate = yearProvidentFundInterestRate / 12;

        BigDecimal totalLoanAmount = in.getTotalLoanAmount();
        // 计算每月还款本金 公式：每月应还本金=贷款本金÷还款月数
        BigDecimal monthlyRepaymentPrincipal = getMonthlyRepaymentPrincipalByPrincipalFirst(totalLoanAmount, totalRepaymentMonth);

        // 利息总额计数
        BigDecimal totalInterestAmount = BigDecimal.ZERO;
        for (int i = 1; i <= totalRepaymentMonth; i++) {

            // 月还款总额 公式：每月偿还本金=(贷款本金÷还款月数)+(贷款本金-已归还本金累计额)×月利率
            BigDecimal monthlyRepaymentTotalAmount = getMonthlyRepaymentTotalAmountByPrincipalFirst(in.getLoanAmount(),
                    totalRepaymentMonth, loanType, monthlyCommercialInterestRate, monthlyProvidentFundInterestRate, totalLoanAmount, i);

            // 月还款利息
            BigDecimal monthlyRepaymentInterest = monthlyRepaymentTotalAmount.subtract(monthlyRepaymentPrincipal).setScale(2, BigDecimal.ROUND_DOWN);
            totalInterestAmount = totalInterestAmount.add(monthlyRepaymentInterest);

            // 月还款信息
            MonthlyRepayment monthlyRepayment = MonthlyRepayment.builder()
                    .index(i)
                    .date(date.plusMonths(i - 1).toString())
                    .totalAmount(monthlyRepaymentTotalAmount)
                    .principal(monthlyRepaymentPrincipal)
                    .interest(monthlyRepaymentInterest)
                    .build();
            monthlyRepaymentAmount.add(monthlyRepayment);
        }

        return Output.builder()
                .input(LoanStructMapper.INSTANCE.convert2InputVo(in))
                .totalAmountOwed(totalInterestAmount.add(totalLoanAmount))
                .totalInterestAmount(totalInterestAmount)
                .monthlyRepaymentAmount(monthlyRepaymentAmount)
                .build();
    }

    /**
     * 等额本金 月还款总额
     *
     * @param loanAmount
     * @param totalRepaymentMonth
     * @param loanType
     * @param monthlyCommercialInterestRate
     * @param monthlyProvidentFundInterestRate
     * @param totalLoanAmount
     * @param index
     * @return
     */
    private static BigDecimal getMonthlyRepaymentTotalAmountByPrincipalFirst(Map<LoanType, BigDecimal> loanAmount,
                                                                             int totalRepaymentMonth,
                                                                             LoanType loanType,
                                                                             double monthlyCommercialInterestRate,
                                                                             double monthlyProvidentFundInterestRate,
                                                                             BigDecimal totalLoanAmount,
                                                                             int index) {
        BigDecimal monthlyRepaymentTotalAmount = BigDecimal.ZERO;
        if (loanType == LoanType.COMMERCIAL_LOAN) {
            monthlyRepaymentTotalAmount = getMonthlyRepaymentTotalAmountByLoanType(totalLoanAmount, monthlyCommercialInterestRate, totalRepaymentMonth, index);
        }
        if (loanType == LoanType.PROVIDENT_FUND_LOAN) {
            monthlyRepaymentTotalAmount = getMonthlyRepaymentTotalAmountByLoanType(totalLoanAmount, monthlyProvidentFundInterestRate, totalRepaymentMonth, index);
        }
        if (loanType == LoanType.BLENDED_LOAN) {
            BigDecimal commercialLoanAmount = loanAmount.get(LoanType.COMMERCIAL_LOAN);
            BigDecimal providentFundLoanAmount = loanAmount.get(LoanType.PROVIDENT_FUND_LOAN);
            // 月还款总额 = 商贷月还总额 + 公积金月还总额
            BigDecimal monthlyRepaymentCommercialLoanAmount = getMonthlyRepaymentTotalAmountByLoanType(commercialLoanAmount, monthlyCommercialInterestRate, totalRepaymentMonth, index);
            BigDecimal monthlyRepaymentProvidentFundLoanAmount = getMonthlyRepaymentTotalAmountByLoanType(providentFundLoanAmount, monthlyProvidentFundInterestRate, totalRepaymentMonth, index);
            monthlyRepaymentTotalAmount = monthlyRepaymentCommercialLoanAmount.add(monthlyRepaymentProvidentFundLoanAmount);
        }
        return monthlyRepaymentTotalAmount;
    }

    /**
     * 等额本金 月还款本金
     * 公式：每月应还本金=贷款本金÷还款月数
     *
     * @param loanAmount          贷款金额
     * @param totalRepaymentMonth 总还款月
     * @return
     */
    private static BigDecimal getMonthlyRepaymentPrincipalByPrincipalFirst(BigDecimal loanAmount, int totalRepaymentMonth) {
        return loanAmount.divide(BigDecimal.valueOf(totalRepaymentMonth), 2, BigDecimal.ROUND_DOWN);
    }

    /**
     * 等额本金 月还款总额
     * 公式：每月偿还本金=(贷款本金÷还款月数)+(贷款本金-已归还本金累计额)×月利率
     *
     * @param loanAmount          贷款金额
     * @param monthlyInterestRate 月利率
     * @param totalRepaymentMonth 总还款月
     * @param index               第几月次还款
     * @return
     */
    private static BigDecimal getMonthlyRepaymentTotalAmountByLoanType(BigDecimal loanAmount, double monthlyInterestRate, int totalRepaymentMonth, int index) {
        BigDecimal monthlyRepaymentPrincipal = getMonthlyRepaymentPrincipalByPrincipalFirst(loanAmount, totalRepaymentMonth);
        return monthlyRepaymentPrincipal.add(
                loanAmount.subtract(monthlyRepaymentPrincipal.multiply(BigDecimal.valueOf(index - 1)))
                        .multiply(BigDecimal.valueOf(monthlyInterestRate)))
                .setScale(2, BigDecimal.ROUND_DOWN);
    }

    /**
     * 校验贷款利率
     *
     * @param loanType         贷款类型
     * @param yearInterestRate 年利率
     */
    private static void verifyLoanRate(LoanType loanType, Map<LoanType, Double> yearInterestRate) {
        Double yearCommercialInterestRate = yearInterestRate.get(LoanType.COMMERCIAL_LOAN);
        Double yearProvidentFundInterestRate = yearInterestRate.get(LoanType.PROVIDENT_FUND_LOAN);
        if (loanType == LoanType.COMMERCIAL_LOAN) {
            if (yearCommercialInterestRate == null) {
                throw new DisplayableException("商贷需要输入商贷利率!");
            }
        }
        if (loanType == LoanType.PROVIDENT_FUND_LOAN) {
            if (yearProvidentFundInterestRate == null) {
                throw new DisplayableException("公积金贷需要输入公积金贷利率!");
            }
        }
        if (loanType == LoanType.BLENDED_LOAN) {
            if (yearCommercialInterestRate == null) {
                throw new DisplayableException("混合贷需要输入商贷利率!");
            }
            if (yearProvidentFundInterestRate == null) {
                throw new DisplayableException("混合贷需要输入公积金贷利率!");
            }
        }
    }


    /**
     * 计算中断贷款 损失/盈利情况
     * 大概估略，非专业计算
     *
     * @param
     * @return
     */
    public static OutputDiff calculateLoanDiff(Input source, InputDiff target) {

//        15000 * 100 = 150w
//
//        0.3 * =150  45w
//
//        3y later
//
//        12000 *100 = 120w
//
//        5000 * 12 * 3 = 18w
//
//        45 + 18 = 63 w
//
//        120 * 0.7 = 84w 0.8 = 96w
//
//        18 ： 5 + 13
//
//        150 - 45 - 5 = 100w
//
//        45 * 0.03 * 3 + 6 * 0.03 * (3 + 2 + 1) = 4.05 + 1.08 = 5.13w
//
//        3y = -63 + -5.13 = 68w + （100 - 84） = -84w


        Output output = calculateLoan(source);
        if (output == null) {
            return null;
        }

        BigDecimal totalDiffAmount = BigDecimal.ZERO;
        BigDecimal inflationAmount = BigDecimal.ZERO;

        List<DiffAmount> diffAmountList = new ArrayList<>();
        for (Title title : Title.values()) {
            BigDecimal amount = BigDecimal.ZERO;
            switch (title) {
                case TOTAL_PRICE_DIFF:
                    amount = target.getCurrentTotalAmount().subtract(source.getTotalAmount());
                    break;
                case DOWN_PAYMENT_INTEREST:
                    amount = source.getDownPaymentAmount().multiply(BigDecimal.valueOf(target.getFixedDepositRate().getValue()))
                            .multiply(BigDecimal.valueOf(target.getFixedDepositRate().getIndex()));
                    // 通货膨胀
                    inflationAmount = inflationAmount.add(calculateInflationAmount(source.getDownPaymentAmount(), target.getInflationRate(), Period.between(source.getStartDate(), target.getEndDate()).getYears()));
                    break;
                case MONTHLY_REPAYMENT_INTEREST:
                    // 第1年 * index年利率 + 第2年 * index - 1年利率 + ... + 第index年 * 1年利率
                    List<MonthlyRepayment> monthlyRepaymentAmount = output.getMonthlyRepaymentAmount();
                    int index = target.getFixedDepositRate().getIndex();
                    // 几年
                    for (int i = 0; i < index; i++) {
                        BigDecimal yearRepaymentTotalAmount = BigDecimal.ZERO;
                        // 每年还款金额
                        for (int x = i * 12; x < (i + 1) * 12; x++) {
                            yearRepaymentTotalAmount = monthlyRepaymentAmount.get(x).getTotalAmount().add(yearRepaymentTotalAmount);
                        }
                        // 年还款额 至今产生的利息
                        DepositRate yearRate = DepositRate.getDepositRateByMonth((index - i) * 12);
                        BigDecimal yearRepaymentInterest = yearRepaymentTotalAmount.multiply(BigDecimal.valueOf(yearRate.getValue())).multiply(BigDecimal.valueOf(index - i));
                        // 通货膨胀
                        inflationAmount = inflationAmount.add(calculateInflationAmount(yearRepaymentTotalAmount, target.getInflationRate(), index - i));
                        // 总还款额产生的利息
                        amount = yearRepaymentInterest.add(amount);
                    }
                    break;
                case TRANSACTION_TAX:
                    amount = target.getTransactionTax();
                    break;
                case INFLATION:
                    // 首付 * 膨胀率 * 年 + 年还款 * 膨胀率 * 年
                    amount = inflationAmount;
                    break;
                case OTHER:
                    amount = target.getOther();
                    break;
                default:
                    break;
            }
            totalDiffAmount = totalDiffAmount.add(amount);

            DiffAmount diffAmount = DiffAmount.builder()
                    .title(title.getName())
                    .amount(amount)
                    .build();
            diffAmountList.add(diffAmount);
        }

        return OutputDiff.builder()
                .inputDiff(target)
                .output(output)
                .totalDiffAmount(totalDiffAmount)
                .diffAmountList(diffAmountList)
                .build();
    }

    private static BigDecimal calculateInflationAmount(BigDecimal sourceAmount, double inflationRate, int year) {

        // TODO
        return BigDecimal.ZERO;
    }






    @Data
    static class Input {
        private BigDecimal unitPrice; // 单价：元
        private Double area; // 面积: 平方米
        private BigDecimal totalAmount; // 总额
        private DownPaymentRatio downPaymentRatio; // 首付比率
        private BigDecimal downPaymentAmount; // 首付金额
        private LoanPeriodInYears loanPeriodInYears; // 年限
        private RepaymentType repaymentType; // 0等额本息，1等额本金
        private LoanType loanType; // 0商贷，1公积金贷，2混合贷
        private Map<LoanType, Double> loanInterestRate; // 利率
        private Map<LoanType, BigDecimal> loanAmount; // 贷款金额
        private BigDecimal totalLoanAmount; // 贷款总额
        private LocalDate startDate; // 开始时间
    }

    @Data
    static class InputVo {
        private String unitPrice; // 单价：元
        private String area; // 面积: 平方米
        private String totalAmount; // 总额
        private String downPaymentRatio; // 首付比率
        private String downPaymentAmount; // 首付金额
        private String loanPeriodInYears; // 年限
        private String repaymentType; // 0等额本息，1等额本金
        private String loanType; // 0商贷，1公积金贷，2混合贷
        private Map<String, String> loanInterestRate; // 利率
        private Map<String, String> loanAmount; // 贷款金额
        private String totalLoanAmount; // 贷款总额
        private String startDate; // 开始时间
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class InputDiff implements Serializable {
        private static final long serialVersionUID = 1490378129279351762L;
        private BigDecimal currentUnitPrice; // 单价：元
        private BigDecimal currentTotalAmount; // 总额
        private BigDecimal transactionTax; // 交易税
        private BigDecimal other; // 其它金额
        private LocalDate endDate; // 结束时间
        private DepositRate fixedDepositRate; // 定期存款利率
        private double inflationRate; // 通货膨胀率
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class Output implements Serializable {
        private static final long serialVersionUID = 1073084266174833453L;
        private InputVo input; // 贷款信息
        private BigDecimal totalAmountOwed; // 欠款总额 = 贷款总额 + 利息总额
        private BigDecimal totalInterestAmount; // 利息总额
        private List<MonthlyRepayment> monthlyRepaymentAmount; // 月供
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class OutputDiff implements Serializable {
        private static final long serialVersionUID = 7251912711090339228L;
        private Output output;
        private InputDiff inputDiff;
        private BigDecimal totalDiffAmount; // 差额
        private List<DiffAmount> diffAmountList; // 差额列表
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class MonthlyRepayment implements Serializable {
        private static final long serialVersionUID = -5326945787398226778L;
        private Integer index; // 索引
        private String date; // 还款日期
        private BigDecimal totalAmount; // 总额
        private BigDecimal principal; // 本金
        private BigDecimal interest; // 利息
    }

    enum LoanPeriodInYears {
        _5YEAR(5),
        _10YEAR(10),
        _20YEAR(20),
        _30YEAR(30);

        private final Integer value;

        LoanPeriodInYears(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }

    enum DownPaymentRatio {
        _0(0),
        _5(0.05),
        _10(0.10),
        _15(0.15),
        _20(0.20),
        _25(0.25),
        _30(0.30),
        _35(0.35),
        _40(0.40),
        _45(0.45),
        _50(0.50),
        _55(0.55),
        _60(0.60),
        _65(0.65),
        _70(0.70),
        _75(0.75),
        _80(0.80),
        _85(0.85),
        _90(0.90),
        _95(0.95),
        _100(1.0),
        ;

        private final double value;

        DownPaymentRatio(double value) {
            this.value = value;
        }

        public double getValue() {
            return value;
        }
    }

    enum RepaymentType {
        INTEREST_FIRST(0, "等额本息"),
        PRINCIPAL_FIRST(1, "等额本金");

        private final Integer value;
        private final String name;

        RepaymentType(Integer value, String name) {
            this.value = value;
            this.name = name;
        }

        public Integer getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    enum LoanType {
        COMMERCIAL_LOAN(0, "商业贷款"),
        PROVIDENT_FUND_LOAN(1, "公积金贷款"),
        BLENDED_LOAN(2, "混合贷款");

        private final int value;
        private final String name;

        LoanType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public Map<LoanType, Double> getLoanInterestRate() {
            Map<LoanType, Double> loanInterestRate = new ConcurrentHashMap<>(1 << 2);
            loanInterestRate.put(COMMERCIAL_LOAN, COMMERCIAL_LOAN_INTEREST_RATE);
            loanInterestRate.put(PROVIDENT_FUND_LOAN, PROVIDENT_FUND_LOAN_INTEREST_RATE);
            return loanInterestRate;
        }

        public Map<LoanType, BigDecimal> getLoanAmount(BigDecimal commercialLoanAmount,
                                                       BigDecimal providentFundLoanAmount) {
            Map<LoanType, BigDecimal> loanAmount = new ConcurrentHashMap<>(1 << 2);
            loanAmount.put(COMMERCIAL_LOAN, commercialLoanAmount);
            loanAmount.put(PROVIDENT_FUND_LOAN, providentFundLoanAmount);
            return loanAmount;
        }

    }

    @Data
    @Builder
    static class DiffAmount {
        private String title;
        private BigDecimal amount;
    }

    enum Title {
        TOTAL_PRICE_DIFF("总价差额"), // 单价差额 * 面积
        DOWN_PAYMENT_INTEREST("首付利息"), // 首付几年时间可以获得的利息（以银行定额存期为准）
        MONTHLY_REPAYMENT_INTEREST("月供利息"), // 月供几年时间可以获得的利息（以银行不定期为准）
        TRANSACTION_TAX("交易税"), // 交易产生的税，如果是自己付 就要算进去，不是就是0
        INFLATION("通货膨胀"), // 如通货膨胀带来的，假设通货膨胀率为3.5%
        OTHER("其它差额"), // 如法拍、折旧 而打折扣
        ;
        private final String name;

        Title(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    enum DepositRate {
        DEMAND_DEPOSIT(0, 0.005),
        FIXED_DEPOSIT_1(1, 0.01),
        FIXED_DEPOSIT_2(2, 0.02),
        FIXED_DEPOSIT_3(3, 0.03),
        FIXED_DEPOSIT_5(5, 0.05),
        FIXED_DEPOSIT_10(10, 0.10),
        ;

        private final int index;
        private double value;

        DepositRate(int index, double value) {
            this.index = index;
            this.value = value;
        }

        public int getIndex() {
            return index;
        }

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }

        public static DepositRate getDepositRateByMonth(int month) {
            int year = month / 12;
            int index = DEMAND_DEPOSIT.getIndex();
            DepositRate rate = DEMAND_DEPOSIT;
            for (DepositRate depositRate : DepositRate.values()) {
                if (year >= depositRate.getIndex()) {
                    if (depositRate.getIndex() >= index) {
                        index = depositRate.getIndex();
                        rate = depositRate;
                    }
                }
            }
            return rate;
        }
    }


    @Mapper
    interface LoanStructMapper {
        LoanStructMapper INSTANCE = Mappers.getMapper(LoanStructMapper.class);

        @Mappings({
                @Mapping(target = "unitPrice", expression = "java(concatUnit(input.getUnitPrice(), \"元\"))"),
                @Mapping(target = "area", expression = "java(concatUnit(input.getArea(), \"平方米\"))"),
                @Mapping(target = "totalAmount", expression = "java(concatUnit(input.getTotalAmount().divide(java.math.BigDecimal.valueOf(10000), 0, java.math.BigDecimal.ROUND_HALF_UP), \"万元\"))"),
                @Mapping(target = "downPaymentRatio", expression = "java(concatUnit((int) (input.getDownPaymentRatio().getValue() * 100), \"%\"))"),
                @Mapping(target = "downPaymentAmount", expression = "java(concatUnit(input.getDownPaymentAmount().divide(java.math.BigDecimal.valueOf(10000), 0, java.math.BigDecimal.ROUND_HALF_UP), \"万元\"))"),
                @Mapping(target = "loanPeriodInYears", expression = "java(concatUnit(input.getLoanPeriodInYears().getValue(), \"年\"))"),
                @Mapping(target = "repaymentType", expression = "java(concatUnit(input.getRepaymentType().getName(), \"\"))"),
                @Mapping(target = "loanType", expression = "java(concatUnit(input.getLoanType().getName(), \"\"))"),
                @Mapping(target = "loanInterestRate", expression = "java(convertMap(input.getLoanInterestRate(), \"%\"))"),
                @Mapping(target = "loanAmount", expression = "java(convertMap(input.getLoanAmount(), \"万元\"))"),
                @Mapping(target = "totalLoanAmount", expression = "java(concatUnit(input.getTotalLoanAmount(), \"元\"))"),
                @Mapping(target = "startDate", expression = "java(input.getStartDate().toString())"),
        })
        InputVo convert2InputVo(Input input);

        @Named("concatUnit")
        default <T> String concatUnit(T name, String unit) {
            return name + unit;
        }

        @Named("convertMap")
        default Map convertMap(Map<LoanType, ? extends Number> name, String unit) {
            Map<String, String> result = new HashMap<>();
            if ("%".equals(unit)) {
                Map<LoanType, Double> map = (Map<LoanType, Double>) name;
                for (Map.Entry<LoanType, Double> entry : map.entrySet()) {
                    result.put(entry.getKey().getName(), entry.getValue() * 100 + unit);
                }
            }
            if ("万元".equals(unit)) {
                Map<LoanType, BigDecimal> map = (Map<LoanType, BigDecimal>) name;
                for (Map.Entry<LoanType, BigDecimal> entry : map.entrySet()) {
                    result.put(entry.getKey().getName(), entry.getValue().divide(BigDecimal.valueOf(10000), 0, BigDecimal.ROUND_HALF_UP) + unit);
                }
            }
            return result;
        }
    }


}
