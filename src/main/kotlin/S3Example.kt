import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.model.ListObjectsRequest
import java.io.File

fun main() {
    val accessKey = "insert your aws access key here"
    val secretKey = "insert your aws secret key here"
    val awsCredentials = BasicAWSCredentials(accessKey, secretKey)

    val s3 = AmazonS3ClientBuilder.standard().withCredentials(
        AWSStaticCredentialsProvider(awsCredentials)
    ).withRegion(Regions.SA_EAST_1).build()

    val newBucket = "zinid-s3-sdk"
    if (!s3.doesBucketExistV2(newBucket)) {
        s3.createBucket(newBucket)
    } else {
        println("bucket already exists")
    }

    println("\nBuckets list...")
    val buckets = s3.listBuckets()
    for (bucket in buckets) {
        println(bucket.name)
    }

    println("\nSending file to $newBucket...")
    val file = File("Amazon-S3-performance.png")
    s3.putObject(newBucket, "amazon-s3.png", file)

    println("\nListing $newBucket objects...")
    val newBucketObjects = s3.listObjects(ListObjectsRequest().withBucketName(newBucket))
    for (objectSummary in newBucketObjects.objectSummaries) {
        println("*${objectSummary.key} - ${objectSummary.size} - ${objectSummary.owner}")
    }

    println("\nDeleting object from $newBucket...")
    s3.deleteObject(newBucket, "amazon-s3.png")

    println("\nListing $newBucket objects after deletion...")
    val newBucketObjects2 = s3.listObjects(ListObjectsRequest().withBucketName(newBucket))
    for (objectSummary in newBucketObjects2.objectSummaries) {
        println("*${objectSummary.key} - ${objectSummary.size} - ${objectSummary.owner}")
    }

    println("\nDeleting bucket $newBucket...")
    s3.deleteBucket(newBucket)

    println("\nBuckets list...")
    val buckets2 = s3.listBuckets()
    for (bucket in buckets2) {
        println(bucket.name)
    }
}
