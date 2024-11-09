package bg.sofia.uni.fmi.mjt.socialnetwork.post;

import bg.sofia.uni.fmi.mjt.socialnetwork.profile.UserProfile;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SocialFeedPost implements Post {
    private static long id = 1;
    private String uniqueID;
    private final UserProfile author;
    private final String content;
    private final LocalDateTime publishedOn;
    private Map<ReactionType, Set<UserProfile>> reactions;

    private void generateUniqueId() {
        this.uniqueID = "Post " + (id++);
    }

    public SocialFeedPost(UserProfile author, String content) {
        this.author = author;
        this.content = content;
        this.publishedOn = LocalDateTime.now();
        this.reactions = new HashMap<ReactionType, Set<UserProfile>>();
        this.generateUniqueId();
    }

    @Override
    public String getUniqueId() {
        return uniqueID;
    }

    @Override
    public UserProfile getAuthor() {
        return author;
    }

    @Override
    public LocalDateTime getPublishedOn() {
        return publishedOn;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public boolean addReaction(UserProfile userProfile, ReactionType reactionType) {
        if (userProfile == null || reactionType == null) {
            throw new IllegalArgumentException("UserProfile or ReactionType is null");
        }
        boolean isUpdated = false;
        for (Map.Entry<ReactionType, Set<UserProfile>> entry:reactions.entrySet()) {

            if (entry.getValue().remove(userProfile)) {
                if (entry.getValue().isEmpty()) {
                    reactions.remove(entry.getKey());
                }
                isUpdated = true;
            }
        }
        reactions.putIfAbsent(reactionType, new HashSet<>());
        reactions.get(reactionType).add(userProfile);

        return !isUpdated;
    }

    @Override
    public boolean removeReaction(UserProfile userProfile) {
        if (userProfile == null) {
            throw new IllegalArgumentException("User null");
        }
        boolean isRemoved = false;
        for (Map.Entry<ReactionType, Set<UserProfile>> entry :reactions.entrySet()) {
            Set<UserProfile> profiles = entry.getValue();
            if (profiles.remove(userProfile)) {
                if (profiles.isEmpty()) {
                    reactions.remove(entry.getKey());
                }
                isRemoved = true;
            }
        }
        return isRemoved;
    }

    @Override
    public Map<ReactionType, Set<UserProfile>> getAllReactions() {
        return Collections.unmodifiableMap(reactions);
    }

    @Override
    public int getReactionCount(ReactionType reactionType) {
        if (reactionType == null) {
            throw new IllegalArgumentException("ReactionType Null");
        }
        return reactions.getOrDefault(reactionType, Set.of()).size();
    }

    @Override
    public int totalReactionsCount() {
        int sum = 0;
        for (Map.Entry<ReactionType, Set<UserProfile>> entry:reactions.entrySet()) {
            sum += entry.getValue().size();
        }
        return sum;
    }
}
