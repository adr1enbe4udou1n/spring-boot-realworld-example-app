package io.okami101.realworld;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.okami101.realworld.cli.SeederCommand;
import picocli.CommandLine;
import picocli.CommandLine.IFactory;

@SpringBootApplication
public class CliApplication implements CommandLineRunner, ExitCodeGenerator {

	@Autowired
	private IFactory factory;

	@Autowired
	private SeederCommand seederCommand;

	private int exitCode;

	public static void main(String[] args) {
		System.exit(SpringApplication.exit(SpringApplication.run(CliApplication.class, args)));
	}

	@Override
	public int getExitCode() {
		return exitCode;
	}

	@Override
	public void run(String... args) throws Exception {
		exitCode = new CommandLine(seederCommand, factory).execute(args);
	}

}
