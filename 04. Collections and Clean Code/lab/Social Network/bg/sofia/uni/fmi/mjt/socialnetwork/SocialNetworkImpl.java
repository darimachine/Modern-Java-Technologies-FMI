package bg.sofia.uni.fmi.mjt.socialnetwork;

import bg.sofia.uni.fmi.mjt.socialnetwork.exception.UserRegistrationException;
import bg.sofia.uni.fmi.mjt.socialnetwork.post.Post;
import bg.sofia.uni.fmi.mjt.socialnetwork.post.SocialFeedPost;
import bg.sofia.uni.fmi.mjt.socialnetwork.profile.Interest;
import bg.sofia.uni.fmi.mjt.socialnetwork.profile.UserProfile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class SocialNetworkImpl implements SocialNetwork {
    private Set<UserProfile> users;
    private List<Post> posts;
    private boolean isProfileRegistered(UserProfile user) {
        return users.contains(user);
    }

    public SocialNetworkImpl() {
        this.users = new HashSet<UserProfile>();
        this.posts = new ArrayList<>();
    }

    @Override
    public void registerUser(UserProfile userProfile) throws UserRegistrationException {
        if (userProfile == null) {
            throw new IllegalArgumentException("userProfile is NULL");
        }
        if (!users.add(userProfile)) {
            throw new UserRegistrationException("It already exists");
        }
    }

    @Override
    public Set<UserProfile> getAllUsers() {
        return Collections.unmodifiableSet(users);
    }

    @Override
    public Post post(UserProfile userProfile, String content) throws UserRegistrationException {
        if (userProfile == null || content == null || content.isBlank()) {
            throw new IllegalArgumentException("Something is null or empty");
        }

        if (!isProfileRegistered(userProfile)) {
            throw new UserRegistrationException("User Profile is not registered");
        }

        Post currentPost = new SocialFeedPost(userProfile, content);
        posts.add(currentPost);
        return currentPost;
    }

    @Override
    public Collection<Post> getPosts() {
        return Collections.unmodifiableList(posts);
    }

    @Override
    public Set<UserProfile> getReachedUsers(Post post) {
        if (post == null) {
            throw new IllegalArgumentException("Post is Null");
        }
        Set<UserProfile> uniqueUsers = new HashSet<>();
        UserProfile author = post.getAuthor();
        Set<UserProfile> visited = new HashSet<>();
        List<UserProfile> queue = new ArrayList<>(author.getFriends());

        while (!queue.isEmpty()) {
            UserProfile user = queue.remove(0);
            if (visited.add(user)) { // Mark as visited if not already visited
                Set<Interest> userInterests = new HashSet<>(user.getInterests());
                userInterests.retainAll(author.getInterests());
                if (!userInterests.isEmpty()) {
                    uniqueUsers.add(user);
                }
                for (UserProfile friend : user.getFriends()) {
                    if (!visited.contains(friend)) {
                        queue.add(friend);
                    }
                }
            }
        }
        return Collections.unmodifiableSet(uniqueUsers);
    }

    @Override
    public Set<UserProfile> getMutualFriends(UserProfile userProfile1, UserProfile userProfile2)
        throws UserRegistrationException {

        if (userProfile1 == null || userProfile2 == null) {
            throw new IllegalArgumentException("One of the users are null");
        }
        if (!isProfileRegistered(userProfile1) || !isProfileRegistered(userProfile2)) {
            throw new UserRegistrationException("One of them is not registered");
        }

        var user1Friends = userProfile1.getFriends();
        var user2Friends = userProfile2.getFriends();
        Set<UserProfile> mutualFriend = new HashSet<>(user1Friends);
        mutualFriend.retainAll(user2Friends);
        return mutualFriend; // make unmodifiable
    }

    @Override
    public SortedSet<UserProfile> getAllProfilesSortedByFriendsCount() {
        SortedSet<UserProfile> sortedUsers = new TreeSet<>(users);
        return sortedUsers;
    }
}
