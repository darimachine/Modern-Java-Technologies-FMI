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
import bg.sofia.uni.fmi.mjt.newsfeed.json.GsonSingleton;

import java.net.HttpURLConnection;
import java.net.http.HttpResponse;

public class ResponseValidator {
    public static void validateResponse(HttpResponse<String> response) {
        if (response == null) {
            throw new RequestFailedException("Response cannot be null");
        }
        if (response.statusCode() != HttpURLConnection.HTTP_OK) {
            ErrorHandler error = GsonSingleton.getInstance().fromJson(response.body(),
                ErrorHandler.class);
            throwCorrectError(error);
        }
    }

    private static void throwCorrectError(ErrorHandler error) {
        String code = error.code();
        String message = error.message();
        switch (code) {
            case "apiKeyDisabled" -> throw new ApiKeyDisabledException("Your API key has been disabled. " + message);
            case "apiKeyExhausted" -> throw new ApiKeyExhaustedException("API Key Exhausted: " + message);
            case "apiKeyInvalid" -> throw new ApiKeyInvalidException("API Key Invalid: " + message);
            case "apiKeyMissing" -> throw new ApiKeyMissingException("API Key Missing: " + message);
            case "parameterInvalid" -> throw new ParameterInvalidException("Invalid Parameter Provided: " + message);
            case "parametersMissing" -> throw new ParametersMissingException("Required Parameters Missing: " + message);
            case "rateLimited" -> throw new RateLimitedException("Rate Limit Exceeded: " + message);
            case "sourcesTooMany" -> throw new SourcesTooManyException("Too Many Sources Specified: " + message);
            case "sourceDoesNotExist" -> throw new SourceDoesNotExistException("Source Does Not Exist: " + message);
            case "unexpectedError" -> throw new UnexpectedErrorException("Internal Server Error");
            default -> throw new RequestFailedException("Request Failed: " + message);
        }
    }
}
