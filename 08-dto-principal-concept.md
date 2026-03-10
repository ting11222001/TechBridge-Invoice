# Where the principal actually comes from in your code

In the filter there is this line:
```
Authentication authentication = tokenProvider.getAuthentication(userId, authorities, request);
```

So the next question is:

What does `getAuthentication()` create?

```
public Authentication getAuthentication(Long userId, List<GrantedAuthority> authorities, HttpServletRequest request) {
    UsernamePasswordAuthenticationToken userPasswordAuthToken =
    new UsernamePasswordAuthenticationToken(
    userService.getUserById(userId),
    null,
    authorities
    );

    userPasswordAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    return userPasswordAuthToken;
}
```

The important part is here:

```
userService.getUserById(userId)
```
That value becomes the principal.

# What UsernamePasswordAuthenticationToken stores

The constructor is:
```
UsernamePasswordAuthenticationToken(
    Object principal,
    Object credentials,
    Collection<? extends GrantedAuthority> authorities
)
```
So:
```
new UsernamePasswordAuthenticationToken(
    userService.getUserById(userId),
    null,
    authorities
);
```
Internally Spring stores:
```
principal = userService.getUserById(userId)
```

Now the key question becomes:

What does `userService.getUserById()` return?

it returns a `UserDTO`:
```
public interface UserService {
    ...
    UserDTO getUserById(Long id);
}
```

Which means later we can safely do:
```
UserDTO user = (UserDTO) authentication.getPrincipal();
```


# The real mental model

Think of it like this pipeline:
```
JWT token
↓
filter extracts userId
↓
userService.getUserById(userId)
↓
UserDTO created
↓
Authentication(principal = UserDTO)
↓
SecurityContextHolder
↓
Controller
↓
authentication.getPrincipal() → UserDTO
```

So the type of principal depends entirely on what we put into `UsernamePasswordAuthenticationToken`.