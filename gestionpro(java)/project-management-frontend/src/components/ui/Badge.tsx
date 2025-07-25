import { cva, type VariantProps } from "class-variance-authority";
import { cn } from "@/lib/utils";

const badgeVariants = cva(
  "inline-flex items-center rounded-full px-2.5 py-0.5 text-xs font-semibold transition-colors focus:outline-none focus:ring-2 focus:ring-ring focus:ring-offset-2",
  {
    variants: {
      variant: {
        default: "bg-primary text-primary-foreground hover:bg-primary/80",
        secondary:
          "bg-secondary text-secondary-foreground hover:bg-secondary/80",
        destructive: "bg-error text-white hover:bg-error/80",
        outline:
          "text-foreground border border-input hover:bg-accent hover:text-accent-foreground",
        success: "bg-success text-white hover:bg-success/80",
        warning: "bg-warning text-white hover:bg-warning/80",
        info: "bg-info text-white hover:bg-info/80",
        // Status-specific variants
        pending: "bg-yellow-100 text-yellow-800 border border-yellow-200",
        inProgress: "bg-blue-100 text-blue-800 border border-blue-200",
        completed: "bg-green-100 text-green-800 border border-green-200",
        cancelled: "bg-gray-100 text-gray-800 border border-gray-200",
        // Priority-specific variants
        highPriority: "bg-red-100 text-red-800 border border-red-200",
        mediumPriority:
          "bg-yellow-100 text-yellow-800 border border-yellow-200",
        lowPriority: "bg-green-100 text-green-800 border border-green-200",
      },
    },
    defaultVariants: {
      variant: "default",
    },
  }
);

export interface BadgeProps
  extends React.HTMLAttributes<HTMLDivElement>,
    VariantProps<typeof badgeVariants> {}

function Badge({ className, variant, ...props }: BadgeProps) {
  return (
    <div className={cn(badgeVariants({ variant }), className)} {...props} />
  );
}

export { Badge, badgeVariants };
