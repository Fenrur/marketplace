package fr.marketplace.controller;

import fr.marketplace.bi.Product;
import fr.marketplace.bi.ProductRepository;
import fr.marketplace.bi.User;
import fr.marketplace.bi.UserRepository;

import javax.money.MonetaryAmount;
import java.net.URI;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class MutableProduct {

    private String sellerName;
    private UUID id;
    private String name;
    private String description;
    private MonetaryAmount price;
    private Set<URI> images;
    private boolean isAvailable;
    private boolean isAvailableByMarketplace;

    public MutableProduct() {
    }

    public MutableProduct(UUID id, String name, String sellerName, String description, MonetaryAmount price, Set<URI> images, boolean isAvailable, boolean isAvailableByMarketplace) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.images = images;
        this.isAvailable = isAvailable;
        this.isAvailableByMarketplace = isAvailableByMarketplace;
        this.sellerName = sellerName;
    }

    public static MutableProduct from(Product product, UserRepository userRepository, ProductRepository productRepository) {
        String sellerName = "unknown";

        try {
            final Optional<UUID> userIdByProductIdOp = productRepository.getUserIdByProductId(product.id());

            final UUID userIdByProductId = userIdByProductIdOp.get();

            final Optional<User> userOp = userRepository.get(userIdByProductId);

            sellerName = userOp.get().username().username();

        } catch (Exception ignored) {

        }

        return new MutableProduct(
                product.id(),
                product.name(),
                sellerName,
                product.description(),
                product.price(),
                product.images(),
                product.isAvailable(),
                product.isAvailableByMarketplace()
        );
    }

    public String getSellerName() {
        return sellerName;
    }

    public MutableProduct setSellerName(String sellerName) {
        this.sellerName = sellerName;
        return this;
    }

    public UUID getId() {
        return id;
    }

    public MutableProduct setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public MutableProduct setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public MutableProduct setDescription(String description) {
        this.description = description;
        return this;
    }

    public MonetaryAmount getPrice() {
        return price;
    }

    public MutableProduct setPrice(MonetaryAmount price) {
        this.price = price;
        return this;
    }

    public Set<URI> getImages() {
        return images;
    }

    public MutableProduct setImages(Set<URI> images) {
        this.images = images;
        return this;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public MutableProduct setAvailable(boolean available) {
        isAvailable = available;
        return this;
    }

    public boolean isAvailableByMarketplace() {
        return isAvailableByMarketplace;
    }

    public MutableProduct setAvailableByMarketplace(boolean availableByMarketplace) {
        isAvailableByMarketplace = availableByMarketplace;
        return this;
    }

    public Product build() {
        return new Product(id, name, description, price, images, isAvailable, isAvailableByMarketplace);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MutableProduct that = (MutableProduct) o;

        return getId() != null ? getId().equals(that.getId()) : that.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "MutableProduct{" +
                "sellerName='" + sellerName + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", images=" + images +
                ", isAvailable=" + isAvailable +
                ", isAvailableByMarketplace=" + isAvailableByMarketplace +
                '}';
    }

    public void setFrom(Product product) {
        this.name = product.name();
        this.description = product.description();
        this.price = product.price();
        this.images = product.images();
        this.isAvailable = product.isAvailable();
        this.isAvailableByMarketplace = product.isAvailableByMarketplace();
    }
}
