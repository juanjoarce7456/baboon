package org.unc.lac.baboon.test.cases;

import org.junit.Test;
import org.unc.lac.baboon.exceptions.BadPolicyException;
import org.unc.lac.baboon.exceptions.BadTopicsJsonFormat;
import org.unc.lac.baboon.exceptions.InvalidGuardProviderMethod;
import org.unc.lac.baboon.exceptions.MultipleGuardProvidersException;
import org.unc.lac.baboon.exceptions.NoTopicsJsonFileException;
import org.unc.lac.baboon.exceptions.NotSubscribableException;

public class ExceptionsTest {

    /**
     * <li> Given I have custom classes that extend Exception </li>
     * <li> When I use its constructors <li>
     * <li> Then no Exceptions should be thrown </li>
     * <li> And test should reach the end of execution </li>
     */
    @SuppressWarnings("unused")
    @Test
    public void testingExceptionConstructors() {
        BadPolicyException bP = new BadPolicyException();
        bP = new BadPolicyException("");
        bP = new BadPolicyException(new Throwable());
        bP = new BadPolicyException("", new Throwable());
        bP = new BadPolicyException("", new Throwable(),false,false);
        
        BadTopicsJsonFormat bTJF = new BadTopicsJsonFormat();
        bTJF = new BadTopicsJsonFormat("");
        bTJF = new BadTopicsJsonFormat(new Throwable());
        bTJF = new BadTopicsJsonFormat("", new Throwable());
        bTJF = new BadTopicsJsonFormat("", new Throwable(),false,false);
        
        InvalidGuardProviderMethod iGPM = new InvalidGuardProviderMethod();
        iGPM = new InvalidGuardProviderMethod("");
        iGPM = new InvalidGuardProviderMethod(new Throwable());
        iGPM = new InvalidGuardProviderMethod("", new Throwable());
        iGPM = new InvalidGuardProviderMethod("", new Throwable(),false,false);
        
        MultipleGuardProvidersException mGPE = new MultipleGuardProvidersException();
        mGPE = new MultipleGuardProvidersException("");
        mGPE = new MultipleGuardProvidersException(new Throwable());
        mGPE = new MultipleGuardProvidersException("", new Throwable());
        mGPE = new MultipleGuardProvidersException("", new Throwable(),false,false);
        
        NoTopicsJsonFileException nTJFE = new NoTopicsJsonFileException();
        nTJFE = new NoTopicsJsonFileException("");
        nTJFE = new NoTopicsJsonFileException(new Throwable());
        nTJFE = new NoTopicsJsonFileException("", new Throwable());
        nTJFE = new NoTopicsJsonFileException("", new Throwable(),false,false);
        
        NotSubscribableException nSE = new NotSubscribableException();
        nSE = new NotSubscribableException("");
        nSE = new NotSubscribableException(new Throwable());
        nSE = new NotSubscribableException("", new Throwable());
        nSE = new NotSubscribableException("", new Throwable(),false,false);
    }

}
