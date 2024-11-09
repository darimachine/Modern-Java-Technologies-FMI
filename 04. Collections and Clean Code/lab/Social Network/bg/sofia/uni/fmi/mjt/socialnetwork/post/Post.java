package bg.sofia.uni.fmi.mjt.socialnetwork.post;

import bg.sofia.uni.fmi.mjt.socialnetwork.profile.UserProfile;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

public interface Post {

    /**
     * Returns the unique id of the post.
     * Each post is guaranteed to have a unique id.
     *
     * @return the unique id of the post
     */
    String getUniqueId();

    /**
     * Returns the author of the post.
     *
     * @return the author of the post
     */
    UserProfile getAuthor();

    /**
     * Returns the date and time when the post was published.
     * A post is published once it is created.
     *
     * @return the date and time when the post was published
     */
    LocalDateTime getPublishedOn();

    /**
     * Returns the content of the post.
     *
     * @return the content of the post
     */
    String getContent();

    /**
     * Adds a reaction to the post.
     * If the profile has already reacted to the post, the reaction is updated to the latest one.
     * An author of a post can react to their own post.
     *
     * @param userProfile  the profile that adds the reaction
     * @param reactionType the type of the reaction
     * @return true if the reaction is added, false if the reaction is updated
     * @throws IllegalArgumentException if the profile is null
     * @throws IllegalArgumentException if the reactionType is null
     */
    boolean addReaction(UserProfile userProfile, ReactionType reactionType);

    /**
     * Removes a reaction from the post.
     *
     * @param userProfile the profile that removes the reaction
     * @return true if the reaction is removed, false if the reaction is not present
     * @throws IllegalArgumentException if the profile is null
     */
    boolean removeReaction(UserProfile userProfile);

    /**
     * Returns all reactions to the post.
     * The returned map is unmodifiable.
     *
     * @return an unmodifiable view of all reactions to the post
     */
    Map<ReactionType, Set<UserProfile>> getAllReactions();

    /**
     * Returns the count of a specific reaction type to the post.
     *
     * @param reactionType the type of the reaction
     * @return the count of reactions of the specified type
     * @throws IllegalArgumentException if the reactionType is null
     */
    int getReactionCount(ReactionType reactionType);

    /**
     * Returns the total count of all reactions to the post.
     *
     * @return the total count of all reactions to the post
     */
    int totalReactionsCount();
}
