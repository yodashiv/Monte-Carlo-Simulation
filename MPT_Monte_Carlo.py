"""This program utilizes a Monte Carlo Simulation to create a means of comparison
for investing $100,000 in (1) an aggressive portfolio and (2) a conservative portfolio"""
import numpy as np

mean_ag = 9.4324/100
std_ag = 15.675/100
mean_con = 6.189/100
std_con = 6.3438/100
years = 20 # 20 year simulation
inflation = 0.035 # annual inflation is 3.5%
trials = 10000
initial = 100000

def monte_carlo_simulation(intial, trials, mean, std):
    """Runs a Monte Carlo Simulation of a portfolio with MEAN and STD for TRIALS number of times.
    Returns inflation-adjusted list of returns (performances) for each simluation"""
    performances = list()
    for _ in range(trials):
        performances.append(run_simulation(initial, years, mean, std))
    return performances

def run_simulation(initial, years, mean, std):
    """Returns the inflation-adjusted returns of a portfolio with MEAN and STD
    over a YEARS period"""
    for _ in range(years):
        initial *= 1 + np.random.normal(mean, std)
    return inflation_adjuster(initial, years, inflation)

def inflation_adjuster(future_value, years, inflation):
    """Accounts for inflation to adjust future dollars into present value dollars"""
    return future_value / (1 + inflation)**years

# Keeping track of the returns of each 20 year simlulation
performances_ag = monte_carlo_simulation(initial, trials, mean_ag, std_ag)
performances_con = monte_carlo_simulation(initial, trials, mean_con, std_con)

std_performance_ag = np.std(performances_ag)
std_performance_con = np.std(performances_con)

#Median 20 years performance
median_performance_ag = np.median(performances_ag)
median_performance_con = np.median(performances_con)

#10% best case (90th percentile)
ninetieth_percentile_ag = np.percentile(performances_ag, 90)
ninetieth_percentile_con = np.percentile(performances_con, 90)

#10% worse case (10th percentile)
tenth_percentile_ag = np.percentile(performances_ag, 10)
tenth_percentile_con = np.percentile(performances_con, 10)

def extract_info(portfolio, median, ninetieth, tenth):
    return f"Portfolio type: {portfolio} \nMedian 20 Yr: {median} \n" \
            f"10% best case: {ninetieth} \n10% worse case: {tenth}"
print(extract_info("Aggresive", median_performance_ag, ninetieth_percentile_ag,
                    tenth_percentile_ag))
print("\n")
print(extract_info("Very Conservative", median_performance_con,
                    ninetieth_percentile_con, tenth_percentile_con))
