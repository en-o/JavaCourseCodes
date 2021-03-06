package java0.conc0303.future;

import java.util.concurrent.CompletableFuture;

public class CompletableFutureDemo {
    
    public static void main(String[] args){
        
        // 1.变换结果  - 返回结果
        System.out.println("=====>1.变换结果");
        // lambada
        String result1 = CompletableFuture.supplyAsync(()->{return "Hello ";}).thenApplyAsync(v -> v + "world").join();
        System.out.println(result1);
    
        // 2.消费 - 不返回结果 内部直接答应结果
        CompletableFuture.supplyAsync(()->{return "Hello ";}).thenAccept(v -> { System.out.println("=====>2.消费");System.out.println("consumer: " + v);});
        
        // 3.组合
        System.out.println("=====>3.组合");
        String result3 = CompletableFuture.supplyAsync(()->{//supplyAsync可以支持返回值 ，runAsync方法不支持返回值。
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Hello";
        }).thenCombine(CompletableFuture.supplyAsync(()->{// thenCombine合并任务
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "world";
        }),(s1,s2)->{return s1 + " " + s2;}).join();
        System.out.println("thenCombine:"+result3);

        // 当一个线程依赖另一个线程时，可以使用 thenApply 方法来把这两个线程串行化
        // thenAccept 消费处理结果  接收任务的处理结果，并消费处理，无返回结果。
        CompletableFuture.supplyAsync(() -> "Hello, java course.")
                .thenApply(String::toUpperCase).thenCompose(s -> CompletableFuture.supplyAsync(s::toLowerCase)).thenAccept(v -> { System.out.println("thenCompose:"+v);});
        
        // 4.竞争
        System.out.println("=====>4.竞争");
        String result4 = CompletableFuture.supplyAsync(()->{
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Hi Boy";
        }).applyToEither(CompletableFuture.supplyAsync(()->{
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Hi Girl";
        }),(s)->{return s;}).join();
        System.out.println(result4);
        
        // 5.补偿异常
        System.out.println("=====>5.补偿异常");
        String result5 = CompletableFuture.supplyAsync(()->{
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(true) {
                throw new RuntimeException("exception test!");
            }
        
            return "Hi Boy";
        }).exceptionally(e->{
            System.out.println(e.getMessage());
            return "Hello world!";
        }).join();
        System.out.println(result5);
        
        
        
    }
    
}
