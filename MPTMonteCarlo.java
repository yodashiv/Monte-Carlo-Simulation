/* This program utilizes a Monte Carlo Simulation to create a means of comparison
for investing $100,000 in (1) an aggressive portfolio and (2) a conservative portfolio */

import java.util.Arrays;
import java.util.Random;
import java.util.HashMap;

public class MPTMonteCarlo{
  static double meanAggressive = 9.4324/100;
  static double stdAggressive = 15.675/100;
  static double meanConservative = 6.189/100;
  static double stdConservative = 6.3438/100;
  static int years = 20; // 20 year simulation
  static double inflation = 0.035; // annual inflation is 3.5%
  static int trials = 10000;
  static double initial = 100000;

  public static void main(String[] args){
    HashMap<String, Double> informationAggressive = extractInfo(initial, trials, meanAggressive, stdAggressive);
    HashMap<String, Double> informationConservative = extractInfo(initial, trials, meanConservative, stdConservative);
    System.out.println(readInfo("Aggressive", informationAggressive));
    System.out.println();
    System.out.println(readInfo("Very Conservative", informationConservative));
  }

  static HashMap<String, Double> extractInfo(double initial, int trials, double mean, double std){
    /** Given the INTIAL value of the portfolio, the numbers of TRIALS, the MEAN return of the portfolio
    and the STD (standard deviation), extracts the Median, NinetyPercentile, and TenthPercentile and Returns
    relevant information in a HashMap */
    HashMap<String, Double> information = new HashMap<String, Double>();
    double[] sortedPerformances = monteCarloSimluations(initial, trials, mean, std);
    information.put("STD", getSTD(sortedPerformances));
    information.put("Median", getPercentile(sortedPerformances, 50));
    information.put("NinetyPercentile", getPercentile(sortedPerformances, 90));
    information.put("TenthPercentile", getPercentile(sortedPerformances, 10));
    return information;
  }

  static String readInfo(String portfolio, HashMap<String, Double> information){
    /** Given the PORTOFLIO type and relevant INFORMATION about the portoflio, returns a string
    displaying the relevant finanical metrics */
    return String.format("Portfolio type: %s \nMedian 20 Yr: %f \n10%% best case: %f \n10%% worse case: %f",
    portfolio, information.get("Median"), information.get("NinetyPercentile"), information.get("TenthPercentile"));
  }

  static double[] monteCarloSimluations(double initial, int trials, double mean, double std){
    /** Runs a Monte Carlo Simulation of a portfolio with INITIAL value and with MEAN and STD for TRIALS
    number of times. Returns inflation-adjusted array of returns (performances) for each simluation */
    double[] performances = new double[trials];
    for (int i = 0; i < trials; i++){
      performances[i] = (runSimulation(initial, years, mean, std));
    }
    Arrays.sort(performances);
    return performances;
  }

  static double runSimulation(double initial, int years, double mean, double std){
    /** Returns the inflation-adjusted returns of a portfolio with MEAN and STD
    over a YEARS period */
    for (int i = 0; i < years; i++){
      initial *= 1 + drawGaussianSample(mean, std);
    }
    return inflationAdjuster(initial, years, inflation);
  }

  static double drawGaussianSample(double mean, double std){
    /** Selects a return for next year by drawing randomly from a Gaussian Distribution
    of potenital returns given MEAN and STD of the portfolio */
    Random r = new Random();
    return (r.nextGaussian() * std) + mean;
  }

  static double inflationAdjuster(double futureValue, int years, double inflation){
    /** Adjusts futureValue of money to presentValue of money */
    return futureValue / Math.pow((1 + inflation), years);
  }

  static double getMean(double[] performances){
    /** Given a list PERFORMANCES of end portfolio values, returns the mean */
    double sum = 0;
    for (double performance : performances){
      sum += performance;
    }
    return sum / performances.length;
  }

  static double getSTD(double[] performances){
    /** Given a list PERFORMANCES of end portfolio values, returns the standard deviation */
    double variance = 0;
    double mean = getMean(performances);
    for (double performance : performances){
      variance += Math.pow((mean - performance), 2);
    }
    variance = variance / performances.length;
    return Math.sqrt(variance);
  }

  static double getPercentile(double[] performances, double percentile){
    /** Given a list PERFORMANCES of end portfolio values and the desired PERCENTILE,
     returns the value corresponding to that PERCENTILE */
    int index;
    double closest = (percentile / 100.0) * performances.length;
    index = (int) Math.ceil(closest);
    return performances[index];
  }

}
