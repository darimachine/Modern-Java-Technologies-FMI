package bg.sofia.uni.fmi.mjt.newsfeed.response;

import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.RequestFailedException;
import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.apierrorcodes.ApiKeyDisabledException;
import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.apierrorcodes.ApiKeyExhaustedException;
import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.apierrorcodes.ApiKeyInvalidException;
import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.apierrorcodes.ApiKeyMissingException;
import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.apierrorcodes.ParameterInvalidException;
import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.apierrorcodes.ParametersMissingException;
import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.apierrorcodes.RateLimitedException;
import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.apierrorcodes.SourceDoesNotExistException;
import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.apierrorcodes.SourcesTooManyException;
import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.apierrorcodes.UnexpectedErrorException;

import org.junit.jupiter.api.Test;

import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ResponseValidatorTest {

    @Test
    void testValidateResponseValid() {

        HttpResponse<String> mockResponse = mock();
        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn("{ \"status\": \"ok\" }");

        assertDoesNotThrow(() -> ResponseValidator.validateResponse(mockResponse));
    }
    @Test
    void testValidateResponseNull() {

        assertThrows(RequestFailedException.class, () -> ResponseValidator.validateResponse(null),
            "Expecting RequestFailedException for null response");

    }

    @Test
    void testValidateResponseApiKeyDisabled() {
        testErrorCode("apiKeyDisabled", ApiKeyDisabledException.class, "Your API key has been disabled.");
    }

    @Test
    void testValidateResponseApiKeyExhausted() {
        testErrorCode("apiKeyExhausted", ApiKeyExhaustedException.class, "API Key Exhausted:");
    }

    @Test
    void testValidateResponseApiKeyInvalid() {
        testErrorCode("apiKeyInvalid", ApiKeyInvalidException.class, "API Key Invalid:");
    }

    @Test
    void testValidateResponseApiKeyMissing() {
        testErrorCode("apiKeyMissing", ApiKeyMissingException.class, "API Key Missing:");
    }

    @Test
    void testValidateResponseParametersInvalid() {
        testErrorCode("parameterInvalid", ParameterInvalidException.class, "Invalid Parameter Provided:");
    }

    @Test
    void testValidateResponseParametersMissing() {
        testErrorCode("parametersMissing", ParametersMissingException.class, "Required Parameters Missing:");
    }

    @Test
    void testValidateResponseRateLimited() {
        testErrorCode("rateLimited", RateLimitedException.class, "Rate Limit Exceeded:");
    }

    @Test
    void testValidateResponseSourceDoesTooMany() {
        testErrorCode("sourcesTooMany", SourcesTooManyException.class, "Too Many Sources Specified:");
    }

    @Test
    void testValidateResponseSourceDoesNotExist() {
        testErrorCode("sourceDoesNotExist", SourceDoesNotExistException.class, "Source Does Not Exist:");
    }

    @Test
    void testValidateResponseUnexpectedError() {
        testErrorCode("unexpectedError", UnexpectedErrorException.class, "Internal Server Error");
    }

    private void testErrorCode(String errorCode, Class<? extends Exception> expectedException, String expectedMessage) {

        HttpResponse<String> mockResponse = mock();
        when(mockResponse.statusCode()).thenReturn(400);

        String errorJson = """
            {
                "code": "%s",
                "message": "Test error message"
            }
        """.formatted(errorCode);

        when(mockResponse.body()).thenReturn(errorJson);

        Exception exception = assertThrows(expectedException, () ->
            ResponseValidator.validateResponse(mockResponse)
        );
        assertTrue(exception.getMessage().contains(expectedMessage),
            "Expecting message to contain the error message");
    }
}
