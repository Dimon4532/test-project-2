package ru.learning.java.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.learning.java.company.CompanyDirectory;
import ru.learning.java.company.ReportGenerator;

@Configuration
public class CompanyConfig {

  @Bean
  public CompanyDirectory companyDirectory() {
    return new CompanyDirectory();
  }

  @Bean
  public ReportGenerator reportGenerator() {
    return new ReportGenerator();
  }
}